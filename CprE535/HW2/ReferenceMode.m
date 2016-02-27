function [ status ] = ReferenceMode( pmode, inPath, cameraID, imageType, denoiseFilter, overwrite )
%UNTITLED Summary of this function goes here
%   Detailed explanation goes here

    outPath = [inPath '_out'];

    % Search for jpeg and png files
    if (~exist('iList') == 1)
        fileData = rdir([inPath, '\*.*'], 'regexp(name, [''.png'' ''|'' ''.jpg''], ''ignorecase'')', true);
        iList = {fileData.name};
    end

    % Create PRNU name

    switch pmode
        case 'averagePRNU'
            imageID = 'AVE';
            prnuName = {[cameraID '_' imageType '_' imageID]};
        case 'singlePRNU'
            nL = length(iList);
            for i=1:nL
                [b1 imageID b2] = fileparts(iList{i}); clear b1 b2;
                prnuName{i} = {[cameraID '_' imageType '_' imageID]};
            end
    end

    denoiseFolderRoot = [inPath '\denoise'];

    addargs.filterName = denoiseFilter;
    switch denoiseFilter

        case 'gaussian'    % Use Gaussian LPF 

            addargs.denoiseFolder = [denoiseFolderRoot '\gaussian'];     % Folder to save denoised images
            addargs.filterSize = [3 3];     % Filter size
            addargs.filterSD = 0.5;         % Gaussian filter standard deviation
            addargs.overwrite = overwrite;          % 0: use denoised file if exists, 1: overwrite file

            switch pmode
                case 'averagePRNU'
                    prnuName{1} = [prnuName{1} '_G'];
                case 'singlePRNU'
                    for i=1:nL
                        prnuName{i} = {[prnuName{i}{:} '_G']};
                    end
            end

        case 'mihcak'       % Use Mihcak wavelet based denoising algorithm

            if ~wavInst
                fprintf('Warning: Mihcak filter selected but Wavelet Toolbox is not available.\n');
            end
            addargs.denoiseFolder = [denoiseFolderRoot '\mihcak'];     % Folder to save denoised images
            addargs.sigma0 = 5;     % value of sigma0
            addargs.overwrite = overwrite;
            addargs.saveWaveletCoeffs = saveWaveletCoeffs;      % Save wavelet coefficients as a .mat file

            switch pmode
                case 'averagePRNU'
                    prnuName{1} = [prnuName{1} '_M'];
                case 'singlePRNU'
                    for i=1:nL
                        prnuName{i} = {[prnuName{i}{:} '_M']};
                    end
            end

        case 'sigma'        % Use Sigma filter

            addargs.denoiseFolder = [denoiseFolderRoot '\sigma'];     % Folder to save denoised images
            addargs.windowSize = 7;         % window size to use in sigma filter
            %addargs.stdval = [2.35 1.6 2.0];           % value for standard deviation of flat field images for Kodak V550
            addargs.stdval = [2.0 2.0 2.0];           % value for standard deviation of flat field images for Kodak V550
            addargs.overwrite = overwrite;          % 0: use denoised file if exists, 1: overwrite file

            switch pmode
                case 'averagePRNU'
                    prnuName{1} = [prnuName{1} '_S'];
                case 'singlePRNU'
                    for i=1:nL
                        prnuName{i} = {[prnuName{i}{:} '_S']};
                    end
            end

        case 'bm3d'     % Use the method of denoising by sparse 3D transform-domain collaborative filtering

            addargs.denoiseFolder = [denoiseFolderRoot '\bm3dFolder'];
            addargs.sigma = 0.4;        % Use a standard deviation of 1.0 (for intensities in range [0 255]
            addargs.overwrite = overwrite;      % use denoised file if it exists

            switch pmode
                case 'averagePRNU'
                    prnuName{1} = [prnuName{1} '_B'];
                case 'singlePRNU'
                    for i=1:nL
                        prnuName{i} = {[prnuName{i}{:} '_B']};
                    end
            end

        otherwise

            fprintf('Unknown filter\n');

    end

    switch pmode
        case 'averagePRNU'
            prnuNameID = {[prnuName{1} '_L']};
            status = lucasdigicamident('reference', inPath, outPath, prnuNameID, iList, addargs);
        case 'singlePRNU'
            for i=1:nL
                prnuNameID = {[prnuName{i}{:} '_L']};
                iListID = iList(i);
                status = lucasdigicamident('reference', inPath, outPath, prnuNameID, iListID, addargs);
            end
    end
end

