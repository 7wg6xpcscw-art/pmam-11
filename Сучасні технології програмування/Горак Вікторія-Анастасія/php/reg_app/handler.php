<?php

if (isset($_POST['user_name'])) {
  $name = $_POST['user_name'];
  echo "<p>name: $name</p>";
} else {
  echo "<p>Дані не були введені.</p>";
}
