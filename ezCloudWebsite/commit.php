<?php
	include_once 'dbconnect_app.php';
	$username = $_POST['username'];
	$repoName = $_POST['repoName'];
	$query = "SELECT repoID FROM  repositories WHERE masterName = '$username' AND repoName = '$repoName'";
	$res = mysqli_query($con,$query);
	mysqli_store_result($con);
	$versionID;
	$repoID;
	if(mysqli_num_rows($res) == 0)	
	{
		//Initial Commit
		//echo "HERE";
		$query = "INSERT INTO repositories ( masterName, repoName) VALUES ('$username' , '$repoName')";
		mysqli_query($con,$query);
		mysqli_store_result($con);
		$query2 = "SELECT repoID FROM repositories WHERE masterName = '$username' AND repoName = '$repoName'"; 
 		$res = mysqli_query($con,$query2);
		mysqli_store_result($con);
		$row = mysqli_fetch_array($res,MYSQLI_NUM);
		$repoID = $row[0];	
		echo $repoID;
		echo " ";
		$versionID = 1;
		$versionLocation = $username."/".$repoName."/";
		$query3 = "INSERT INTO versions(version_location, repoID) VALUES ('$versionLocation','$repoID')";
		mysqli_query($con,$query3);
		mysqli_store_result($con);
	}
	else
	{		
		//LATER COMMITS
		//echo "NOT HERE";
		$row = mysqli_fetch_array($res,MYSQLI_NUM);
		$repoID = $row[0];
		echo $repoID;
		echo " ";
		$query = "SELECT versionNum FROM versions WHERE repoID = '$repoID'";
		$res = mysqli_query($con,$query);
		mysqli_store_result($con);
		$versionID = mysqli_fetch_array($res,MYSQLI_NUM)[0];
		$versionID++;
		$query2 = "UPDATE versions SET versionNum = '$versionID' WHERE repoID = '$repoID'";
		mysqli_query($con,$query2);
		mysqli_store_result($con);
	}
	echo $versionID;
?>