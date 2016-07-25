<?php
include_once 'dbconnect_app.php';
{
  $sender = $_POST['sender'];
  $receiver = $_POST['receiver'];
  $repo_sender_name = $_POST['repo_sender_name'];
  $repo_receiver_name = $_POST['repo_receiver_name'];
  $details = $_POST['details'];
  $query1 = "SELECT  username FROM users WHERE username =  '$receiver'";
  $res = mysqli_query($con , $query1);
  mysqli_store_result($con);
  if (mysqli_num_rows($res) == 0)
  {
    echo "no receiver found";
    echo "$sender"."<br>"."$receiver"."<br>"."$repo_sender_name"."<br>"."$repo_receiver_name"."<br>"."$details";
  }
  else
  {
    $query = "INSERT INTO messages( `sender`, `receiver`, `repo_sender_name`,`repo_receiver_name`, `details`)VALUES ('$sender','$receiver','$repo_sender_name','$repo_receiver_name','$details')";
    $res = mysqli_query($con,$query);
    mysqli_store_result($con);
    echo "succesfull";
  }
}
?>
