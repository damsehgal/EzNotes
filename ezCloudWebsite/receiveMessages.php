<?php
include_once 'dbconnect_app.php';
    $receiver = $_POST['receiver'];

    $query = "SELECT * FROM messages WHERE receiver = '$receiver' ORDER BY message_time DESC";
    if ($result=mysqli_query($con,$query))
    {
        while ($row = mysqli_fetch_assoc($result))
        {
            $jsonObj[] = $row;
        }
    }
    print json_encode($jsonObj);
    $query = "UPDATE `messages` SET `isRead`= 1 WHERE (`receiver` = '$receiver' AND `isRead` = '0')";
    mysqli_query($con,$query);
    mysqli_store_result($con);
?>
