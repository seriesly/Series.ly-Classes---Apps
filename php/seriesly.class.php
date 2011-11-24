<?php
/*
**********************
*** SERIES.LY API ****
**********************

function userLogin()					http://seriesly.pbworks.com/w/page/41577968/user_token
function userLogout()					Finaliza la sesión del usuario
function getAuth()					http://seriesly.pbworks.com/w/page/43766803/auth_token
function setUserToken($token)				//Usada para poner el user_token en sesión
function getMovieInfo($idFilm)				http://seriesly.pbworks.com/ficha-peli
function getSerieInfo($idSerie,$caps=1)			http://seriesly.pbworks.com/ficha-serie
function getCapsSerie($idSerie)				http://seriesly.pbworks.com/capsSerie
function getLinksCap($idCap)				http://seriesly.pbworks.com/links_cap
function searchMovies($text)				//Acortamiento para buscar pelis	
function searchSeries($text)				//Acortamiento para buscar series	
function apiSearch($text, $type)			http://seriesly.pbworks.com/buscar	
function getVideoUrl($enc)				http://seriesly.pbworks.com/cargar_enlace
function userSeries()					http://seriesly.pbworks.com/user-mis_series
function userMovies()					http://seriesly.pbworks.com/user-mis_pelis
function getURL($url, $cookie=false)			//Usada para las peticiones curl
function isUserLogged($force=false)			http://seriesly.pbworks.com/user-estado
function isAuth()					//Comprobar si la app tiene el auth_token

Para poder hacer uso de esta class debes ser dev registrado en series.ly
Revisar http://seriesly.pbworks.com/ para obtener el correo de contacto y novedades.

*/
if (!function_exists('curl_init')) {
	echo 'series.ly needs the CURL PHP extension.';
	exit();
}
if (!function_exists('json_decode')) {
	echo 'series.ly needs the JSON PHP extension.';
	exit();
}

class seriesly
{
	private $api_id;
	private $secret;
	private $baseURL = "http://series.ly/api/";
	private $format;
	private $user_token = false;
	public $isAuth = false;
	public $auth_token = "";
	
	public function __construct () 
	{
		$this->api_id = $GLOBALS["api_id"];
		$this->secret = $GLOBALS["secret"];
		
		if($GLOBALS["format"]=="xml")
			$this->format = "xml";
		else
			$this->format = "json";
			
		$this->getAuth();
	}
	
	public function userLogin()
	{
		if(empty($this->user_token))
		{
			$url = $this->baseURL."authUser.php?auth_token=".$this->auth_token;
			echo "<script language='javascript' src='".$this->baseURL."js/login.js'></script><script language='javascript'>api_login('".$this->auth_token."','".urlencode($GLOBALS["callback_url"])."')</script>";
		}
	}
	
	public function userLogout()
	{
		$this->user_token=false;
		unset($_SESSION["user_token"]);
	}
	
	public function getAuth()
	{
		$oAuth=$this->getURL($this->baseURL."auth.php?api=".$this->api_id."&secret=".$this->secret);

		if(strlen($oAuth) != 32)
		{
			return(false);
		}
		else
		{
			$this->auth_token = $oAuth;
			$this->isAuth=true;			
			if(isset($_SESSION["user_token"])) $this->user_token=$_SESSION["user_token"];
			return(true);
		}
	}
	
	public function setUserToken($token)
	{
		$this->user_token=$token;
		$_SESSION["user_token"]=$this->user_token;
	}
	
	public function getMovieInfo($idFilm)
	{	
		if(empty($this->user_token))
			return json_decode($oAuth=$this->getURL($this->baseURL."detailMovie.php?auth_token=".$this->auth_token."&idFilm=".$idFilm."&format=".$this->format));
		else
			return json_decode($oAuth=$this->getURL($this->baseURL."detailMovie.php?auth_token=".$this->auth_token."&idFilm=".$idFilm."&user_token=".$this->user_token."&format=".$this->format));
	}
	
	public function getSerieInfo($idSerie,$caps=1)
	{
		if(empty($this->user_token))
			return json_decode($oAuth=$this->getURL($this->baseURL."detailoSerie.php?auth_token=".$this->auth_token."&idSerie=".$idSerie."&caps=".$caps."&format=".$this->format));
		else
			return json_decode($oAuth=$this->getURL($this->baseURL."detailoSerie.php?auth_token=".$this->auth_token."&idSerie=".$idSerie."&caps=".$caps."&user_token=".$this->user_token."&format=".$this->format));
	}
	public function getCapsSerie($idSerie)
	{
		return json_decode($oAuth=$this->getURL($this->baseURL."capsSerie.php?auth_token=".$this->auth_token."&idSerie=".$idSerie."&user_token=".$this->user_token."&format=".$this->format));
	}
	public function getLinksCap($idCap)
	{
		if(empty($this->user_token))
			return 'empty user_token';
		else
			return json_decode($oAuth=$this->getURL($this->baseURL."linksCap.php?auth_token=".$this->auth_token."&idCap=".$idCap."&user_token=".$this->user_token."&format=".$this->format));
	}
	public function searchMovies($text) 
	{
		return($this->apiSearch($text, "film"));
	}
	
	public function searchSeries($text) 
	{
		return($this->apiSearch($text, "serie"));
	}
	
	private function apiSearch($text, $type)
	{
		return json_decode($oAuth=$this->getURL($this->baseURL."search.php?auth_token=".$this->auth_token."&search=".urlencode($text)."&type=".$type."&format=".$this->format));
	}
	
	public function getVideoUrl($enc)
	{
		return $this->baseURL."goLink.php?auth_token=".$this->auth_token."&user_token=".$this->user_token."&enc=".$enc;
	}
	
	public function userSeries()
	{
		return json_decode($oAuth=$this->getURL($this->baseURL."userSeries.php?auth_token=".$this->auth_token."&user_token=".$this->user_token."&format=".$this->format));
	}
	
	public function userMovies()
	{
		return json_decode($oAuth=$this->getURL($this->baseURL."userMovies.php?auth_token=".$this->auth_token."&user_token=".$this->user_token."&format=".$this->format));
	}
	
	private function getURL($url, $cookie=false)
	{
		$cURLHandle = curl_init();
		curl_setopt($cURLHandle, CURLOPT_URL, $url); 
	    curl_setopt($cURLHandle, CURLOPT_RETURNTRANSFER, true);
	    curl_setopt($cURLHandle, CURLOPT_TIMEOUT, 60);
	    $response = curl_exec($cURLHandle);
	    curl_close($cURLHandle);
	    return(trim($response));
	}
	
	public function isUserLogged($force=false)
	{
		if($this->user_token!=false)
		{
			if($force)
			{
				if( ($this->getURL($this->baseURL."isUserLogged.php?auth_token=".$this->auth_token."&user_token=".$this->user_token)) == "1")
					return(true);
				else
					return(false);
			}
			else
				return(true);
		}
		else
			return(false);
	}
	
	public function isAuth()
	{
		return($this->isAuth);
	}
}
?>
