<?php
	include_once 'dbconnect_app.php';
	$reponame = $_POST['reponame'];
	$username = $_POST['username'];
	$sql = "SELECT versionNum FROM `versions` WHERE `repoID` = ( select repoID from repositories where `repoName` = '$reponame' AND `masterName` = '$username' )";

	$res = mysqli_query($con,$sql); 
	mysqli_store_result($con);
  	$row = mysqli_fetch_array($res ,MYSQLI_NUM);
  	$version_num = $row[0];
  	echo "$version_num";
?>