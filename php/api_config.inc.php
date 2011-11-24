<?php

session_start();

$api_id 		= "";								/* Inserta la id de tu app aquí */
$secret 		= "";								/* Inserta la key aquí */
$callback_url 	= "http://tuweb.com/callback.php";
$format 		= "json";							/* xml o (default) json */

require_once("seriesly.class.php");


?>