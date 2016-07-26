<?php
    include_once 'dbconnect_app.php';
    {
        $username = $_POST['username'];
        $sql = "SELECT repoName FROM `repositories` WHERE `masterName` = '$username'";
        if ($result=mysqli_query($con,$sql))
        {
            while ($row = mysqli_fetch_assoc($result))
            {
                $jsonObj[] = $row;
            }
        }
        print json_encode($jsonObj);
    }
?>
