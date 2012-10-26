<?
$deviceToken=str_replace(" ", "", $_GET['deviceToken']);

$payload['aps'] = array('alert' => 'Congratulations!', 'badge' => 6, 'sound' => 'default');
$payload = json_encode($payload);

$apnsHost = 'gateway.sandbox.push.apple.com';
$apnsPort = 2195;
$apnsCert = 'apns-dev.pem';

$streamContext = stream_context_create();
stream_context_set_option($streamContext, 'ssl', 'local_cert', $apnsCert);

$apns = stream_socket_client('ssl://' . $apnsHost . ':' . $apnsPort, $error, $errorString, 2, STREAM_CLIENT_CONNECT, $streamContext);

$apnsMessage = chr(0) . chr(0) . chr(32) . pack('H*', str_replace(' ', '', $deviceToken)) . chr(0) . chr(strlen($payload)) . $payload;
fwrite($apns, $apnsMessage);

fclose($apns);

?>		