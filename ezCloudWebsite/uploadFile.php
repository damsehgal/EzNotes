<?php
include_once 'keys.php';
$ftp_server = "ezcloud.esy.es";
$ftp_conn = ftp_connect($ftp_server) or die("Could not connect to $ftp_server");
$login = ftp_login($ftp_conn, "u386866041", $password);
$username = $_GET['username'];
$uploads_dir = "/home/u386866041/public_html/ezCloudWebsite/".$username."/";
echo $username;
if (!file_exists($uploads_dir)) 
{
    mkdir($uploads_dir, 0777, true);
}
if (is_uploaded_file($_FILES['bill']['tmp_name'])) 
{
	$tmp_name = $_FILES['bill']['tmp_name'];
	$pic_name = $_FILES['bill']['name'];
	move_uploaded_file($tmp_name, $uploads_dir.$pic_name);
}
ftp_close($ftp_conn);
?>

