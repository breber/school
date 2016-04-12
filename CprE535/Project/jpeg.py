import struct
import sys

# https://en.wikipedia.org/wiki/JPEG
# https://en.wikipedia.org/wiki/JPEG_File_Interchange_Format
# https://en.wikibooks.org/wiki/JPEG_-_Idea_and_Practice/The_header_part
# http://www.xbdev.net/image_formats/jpeg/050_dec_enc_demo/index.php

class JPEG:
    def parse_dqt(self, byte_data):
        # Length (2 bytes)
        # Table:
        #   Precision (4 bits)
        #   Index (4 bits)
        #   TableData (64 bytes)
        index = 0
        length = byte_data[index] << 8 | byte_data[index + 1]
        index = index + 2

        tables = {}

        while length - index > 0:
            precision = byte_data[index] >> 4
            table_index = byte_data[index] & 0xF
            index = index + 1

            tables[table_index] = {
                'precision': precision,
                'table': byte_data[index:index + 64]
            }
            index = index + 64

        # print('parse_dqt: num_tables: %d' % len(tables))
        # for table in tables:
        #     print('parse_dqt: table[%d]: precision: %d data: %s' % \
        #         (table, tables[table]['precision'], tables[table]['table']))

        return tables

    def parse_dht(self, byte_data):
        # Length (2 bytes)
        # Table:
        #   AC/DC (4 bits)
        #   Destination Identifier (4 bits)
        #   Block Lengths (16 bytes)
        #   HuffEncodedValues (sum(block lengths) bytes)
        index = 0
        length = byte_data[index] << 8 | byte_data[index + 1]
        index = index + 2

        tables = {}

        while length - index > 0:
            ac_dc = byte_data[index] >> 4
            dest_id = byte_data[index] & 0xF
            index = index + 1

            table = {
                'lengths': [0],
                'blocks': []
            }

            # Build the basic information for the tables (including counts)
            num_blocks = 0
            for i in range(0, 16):
                table['lengths'].append(byte_data[index + i])
                for j in range(0, table['lengths'][i + 1]):
                    table['blocks'].append({
                        'length': i + 1
                    })
                num_blocks = num_blocks + table['lengths'][i + 1]
            index = index + 16

            # Generate the huffman codes
            length_count = 1
            code = 0
            for i in range(0, num_blocks):
                while length_count < table['blocks'][i]['length']:
                    code = code << 1
                    length_count = length_count + 1

                table['blocks'][i]['code'] = code
                table['blocks'][i]['value'] = byte_data[index + i]
                code = code + 1

            index = index + num_blocks

            tables[(ac_dc, dest_id)] = table

            # print('parse_dht: table[(%d, %d)]:' % (ac_dc, dest_id))
            # for j in range(0, len(tables[(ac_dc, dest_id)]['blocks'])):
            #     print('    %s' % tables[(ac_dc, dest_id)]['blocks'][j])

        return tables

    def parse_sos(self, byte_data):
        # Length (2 bytes)
        # Num Components (1 byte)
        # Component:
        #   ID (1 byte)
        #   DC Selector (4 bits)
        #   AC Selector (4 bits)
        #   Start of spectral selection (1 byte)
        #   End of spectral selection (1 byte)
        #   Successive Approximation Bit High (4 bits)
        #   Successive Approximation Bit Low (4 bits)
        index = 0
        length = byte_data[index] << 8 | byte_data[index + 1]
        index = index + 2

        num_components = byte_data[index]
        index = index + 1

        components = {}
        for i in range(0, num_components):
            component_id = byte_data[index]
            index = index + 1

            ac_selector = byte_data[index] >> 4
            dc_selector = byte_data[index] & 0xF
            index = index + 1

            components[component_id] = {
                'ac_selector': ac_selector,
                'dc_selector': dc_selector
            }

        # print('parse_sos: num_components: %d' % num_components)
        # for c in components:
        #     print('parse_sos: components[%d]: %s' % \
        #         (c, components[c]))

        self.parse_image_data(byte_data[index + 3:], components)

        return components

    def parse_sof(self, byte_data):
        # Length (2 bytes)
        # Sample Precision (1 byte)
        # Num Lines (2 bytes)
        # Samples per line (2 bytes)
        # Num Components (1 byte)
        # Component:
        #   ID (1 byte)
        #   Horizontal Sample Factor (4 bits)
        #   Vertical Sample Factor (4 bits)
        #   Quanization Table Selector (1 byte)
        index = 0
        length = byte_data[index] << 8 | byte_data[index + 1]
        index = index + 2

        sample_precision = byte_data[index]
        index = index + 1

        num_lines = byte_data[index] << 8 | byte_data[index + 1]
        index = index + 2

        samples_per_line = byte_data[index] << 8 | byte_data[index + 1]
        index = index + 2

        num_components = byte_data[index]
        index = index + 1

        components = {}

        for i in range(0, num_components):
            component_id = byte_data[index]
            index = index + 1

            horiz_sample_factor = byte_data[index] >> 4
            vert_sample_factor = byte_data[index] & 0xF
            index = index + 1

            quant_table_selector = byte_data[index]
            index = index + 1

            components[component_id] = {
                'horiz_sample_factor': horiz_sample_factor,
                'vert_sample_factor': vert_sample_factor,
                'quant_table_selector': quant_table_selector
            }

        # print('parse_sof: num_components: %d' % num_components)
        # for c in components:
        #     print('parse_sof: components[%d]: %s' % \
        #         (c, components[c]))

        return {
            'width': samples_per_line,
            'height': num_lines,
            'components': components
        }

    def get_n_bits(self, byte_data, offset_bits, bits):
        offset_bytes = int(offset_bits / 8)
        offset_bits = int(offset_bits % 8)
        num_bytes = int(bits / 8)
        num_bits = int(bits % 8)

        result = byte_data[offset_bytes] >> (8 - offset_bits)
        start_byte = result
        for b in range(0, num_bytes):
            result = result << 8 | byte_data[offset_bytes + b]

        result = result << num_bits
        end_byte = byte_data[offset_bytes + num_bytes]
        end_byte = end_byte >> (8 - num_bits)
        result = result | end_byte

        print('get_n_bits: %d, %d --> %s' % (offset_bits, bits, bin(result)))
        print('get_n_bits: bytes: %d, %d --> %s' % (offset_bytes, num_bytes, bin(start_byte)))

        return result

    def is_huffman_code(self, blocks, code, code_bits):
        for j in range(0, len(blocks)):
            if code == blocks[j]['code'] and code_bits == blocks[j]['length']:
                return (True, blocks[j]['value']);

        return (False, None);

    def process_huffman_unit(self, byte_data, index, component, sos_component):
        dct = [0 for i in range(0, 64)]
        # print('process_huffman_unit: %s' % byte_data[index:index + 16])
        # print('process_huffman_unit: %d' % byte_data[index])
        # print('process_huffman_unit: %s' % component)

        print('byte_data: %s' % [hex(l) for l in byte_data[0:10]])
        print('byte_data: %s' % [bin(l) for l in byte_data[0:10]])

        blocks = self.current_scan['DHT_parsed'][(0, sos_component['ac_selector'])]['blocks']

        print('blocks:')
        for j in range(0, len(blocks)):
            print('    %s' % blocks[j])

        offset_bits = 0
        found = False
        for k in range(1, 16):
            code = self.get_n_bits(byte_data, offset_bits, k)

            (is_code, result) = self.is_huffman_code(blocks, code, k)

            print('code: %d --> %s' % (code, is_code))
            if is_code:
                offset_bits = k
                found = True

                if result == 0:
                    dct[0] = component['previous_val']
                else:
                    data = self.get_n_bits(byte_data, offset_bits, result)
                    print('data: %d' % data)
                    offset_bits = offset_bits + result

                    dct[0] = data + component['previous_val']
                    component['previous_val'] = dct[0]

                break

        print('DCT[0]: %d' % dct[0])

        return index, dct

    def decode_block(self, component, dct, stride):
        return []
        zig_zag_array = [
            0,   1,   5,  6,   14,  15,  27,  28,
            2,   4,   7,  13,  16,  26,  29,  42,
            3,   8,  12,  17,  25,  30,  41,  43,
            9,   11, 18,  24,  31,  40,  44,  53,
            10,  19, 23,  32,  39,  45,  52,  54,
            20,  22, 33,  38,  46,  51,  55,  60,
            21,  34, 37,  47,  50,  56,  59,  61,
            35,  36, 48,  49,  57,  58,  62,  63
        ]

        quant_table_selector = component['quant_table_selector']
        dqt_table = self.current_scan['DQT_parsed'][quant_table_selector]['table']

        print dct
        print dqt_table
        # Copy quantization table locally
        data = [i for i in dct]

        # De-quantize the block
    	for i in range(0, 64):
            data[i] = data[i] * dqt_table[i]

        # De-zig-zag the block
        de_zig_zagged = []
       	for i in range(0, 64):
            de_zig_zagged.append(data[zig_zag_array[i]])

        # Transform to 8x8
        # TODO

        # Inverse the DCT (idct)
        import math
        idct_res = math.idct(de_zig_zagged)

        # Level Shift each element (i.e. add 128)
        print('Decoded 8x8 Block')
        print('stride: %d' % stride)
        for y in range(0, 8):
            for x in range(0, 8):
                index = x + y * stride / 2
                # print('x: %d, y: %d, index: %d' % (x, y, index))
                # idct_res[index] = idct_res[index] + 128

            print('%s' % idct_res[y * stride / 2:y * stride / 2 + stride / 2])

        return idct_res

    def parse_image_data(self, byte_data, sos):
        sof_parsed = self.current_scan['SOF0_parsed']
        y_component = sof_parsed['components'][1]
        cb_component = sof_parsed['components'][2]
        cr_component = sof_parsed['components'][3]

        y_component['previous_val'] = 0
        cb_component['previous_val'] = 0
        cr_component['previous_val'] = 0

        sos_y = sos[1]
        sos_cb = sos[2]
        sos_cr = sos[3]

        width = sof_parsed['width']
        height = sof_parsed['height']

        x_stride = 8 * y_component['horiz_sample_factor']
        y_stride = 8 * y_component['vert_sample_factor']

        index = 0
        rgb = [[0 for x in range(height)] for x in range(width)]
        for y in range(0, height, y_stride):
            for x in range(0, width, x_stride):
                y_com = []

                # Decode MCU
                for y1 in range(0, y_stride / 8):
                    for x1 in range(0, x_stride / 8):
                        # Process Huffman unit (y_component)
                        (index, dct) = self.process_huffman_unit(byte_data, index, y_component, sos_y)
                        y_com.append(self.decode_block(y_component, dct, x_stride))

                break

                # Process Huffman unit (cb_component)
                (index, dct) = self.process_huffman_unit(byte_data, index, cb_component, sos_cb)
                cb = self.decode_block(y_component, dct, x_stride)

                # Process Huffman unit (cr_component)
                (index, dct) = self.process_huffman_unit(byte_data, index, cr_component, sos_cr)
                cr = self.decode_block(y_component, dct, x_stride)

                # Convert YCbCr to RGB
                for y1 in range(0, y_stride):
                    for x1 in range(0, x_stride):
                        if x + x1 >= width:
                            continue
                        if y + y1 >= height:
                            continue

                        offset = x1 / x_stride + y1 / y_stride * 8
                        y_offset = x1 + y1 * y_stride * 8
                        r = y + 1.402 * (cb[offset] - 128)
                        g = y - 0.34414 * (cr[offset] - 128) - 0.71414 * (cb[offset] - 128)
                        b = y + 1.772 * (cr[offset] - 128)

                        rgb[x][y] = (r, g, b)
            break


    markers = {
        0xFFD8 : { "name": "SOI" },
        0xFFE0 : { "name": "APP0" },
        0xFFDA : { "name": "SOS", "parse": parse_sos },
        0xFFD9 : { "name": "EOI" },
        0xFFDB : { "name": "DQT", "parse": parse_dqt },
        0xFFC4 : { "name": "DHT", "parse": parse_dht },
        0xFFC0 : { "name": "SOF0", "parse": parse_sof }
    }

    app_data = {}
    current_scan = {}
    scans = []
    index = 0

    def handle_marker(self, marker, byte_data):
        # App Data
        if marker >= 0xFFE0 and marker <= 0xFFEF:
            self.app_data[marker] = byte_data
        elif marker in self.markers:
            marker_str = self.markers[marker]['name']
            self.current_scan[marker_str] = byte_data

            if "parse" in self.markers[marker]:
                self.current_scan['%s_parsed' % marker_str] = \
                    self.markers[marker]["parse"](self, byte_data)

        # print 'handle_marker: %04x --> %02x (%d bytes)' \
        #     % (marker, byte_data[len(byte_data) - 1], len(byte_data))
        # self.index = self.index + 1
        # with open('%x_%d.bin' % (marker, self.index), 'wb') as f:
        #     for b in byte_data:
        #         f.write(struct.pack('B', b))

    def parse(self, image_name):
        byte_data = []
        with open(image_name, "rb") as f:
            byte_data = [ord(x) for x in f.read()]

        last_index = 0
        for index in range(0, len(byte_data) - 1):
            if index != len(byte_data):
                byte_int = byte_data[index]
                next_byte_int = byte_data[index + 1]

                # if the current byte is 0xFF and the next byte isn't 0x00
                # (0x00 is used as an escape character inside the image data
                # when there is actually a 0xFF to distinguish it from a marker)
                # look up the marker information
                if byte_int == 0xFF and next_byte_int != 0x00:
                    if last_index != index:
                        marker = byte_data[last_index - 2] << 8 | byte_data[last_index - 1]
                        self.handle_marker(marker, byte_data[last_index:index])

                    # combine the current byte and the next byte
                    # to determine the marker this corresponds to
                    joined_byte = byte_int << 8 | next_byte_int
                    marker = 'unknown'
                    if joined_byte in self.markers:
                        marker = self.markers[joined_byte]['name']

                    # output the marker information
                    print '%02x%02x --> %s (%d bytes)' \
                        % (byte_int, next_byte_int, marker, index - last_index)

                    # if marker is EOI, copy current_scan to scans
                    if joined_byte == 0xFFD9:
                        self.scans.append(self.current_scan)
                        self.current_scan = {}

                    # manually increment index here to skip past the 'next'
                    # byte that was already part of the marker
                    index = index + 1

                    # save the last index we found a marker (start of the data
                    # after the marker)
                    last_index = index + 1

        exit()

        for scan_index in range(0, len(self.scans)):
            with open('scan_%d.jpg' % scan_index, 'wb') as f:
                # Write SOI
                f.write(struct.pack('B', 0xFF))
                f.write(struct.pack('B', 0xD8))

                # # Write APP data
                # for app_data in self.app_data:
                #     f.write(struct.pack('B', app_data >> 8))
                #     f.write(struct.pack('B', app_data & 0xFF))
                #     for b in self.app_data[app_data]:
                #         f.write(struct.pack('B', b))
                #
                # # Write SOI
                # f.write(struct.pack('B', 0xFF))
                # f.write(struct.pack('B', 0xD8))

                # Write DQT
                if 'DQT' in self.scans[scan_index]:
                    f.write(struct.pack('B', 0xFF))
                    f.write(struct.pack('B', 0xDB))
                    for b in self.scans[scan_index]['DQT']:
                        f.write(struct.pack('B', b))

                # Write SOF0 (baseline DCT)
                if 'SOF0' in self.scans[scan_index]:
                    f.write(struct.pack('B', 0xFF))
                    f.write(struct.pack('B', 0xC0))
                    for b in self.scans[scan_index]['SOF0']:
                        f.write(struct.pack('B', b))

                # Write DHT
                if 'DHT' in self.scans[scan_index]:
                    f.write(struct.pack('B', 0xFF))
                    f.write(struct.pack('B', 0xC4))
                    for b in self.scans[scan_index]['DHT']:
                        f.write(struct.pack('B', b))

                # Write scan data
                if 'SOS' in self.scans[scan_index]:
                    f.write(struct.pack('B', 0xFF))
                    f.write(struct.pack('B', 0xDA))
                    for b in self.scans[scan_index]['SOS']:
                        f.write(struct.pack('B', b))

                # Write EOI
                f.write(struct.pack('B', 0xFF))
                f.write(struct.pack('B', 0xD9))

def main():
    if len(sys.argv) < 2:
        print("usage: %s file_name" % sys.argv[0])
        exit()

    # import Image
    # img = Image.open(sys.argv[1])
    # print img.bits
    # print img.size
    # print img.format

    jpeg_file = JPEG()
    jpeg_file.parse(sys.argv[1])

if __name__ == "__main__":
    main()
