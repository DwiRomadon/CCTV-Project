package com.example.dwi.cctv_view.server;

//This class is for storing all URLs as a model of URLs

public class Config_URL
{
	//Base URL
	//public static String base_URL = "http://172.16.1.248:10/Api_Siater/v2/";
	//public static String base_URL = "http://10.0.2.2/Api_Siater/v2/";
	public static String base_URL = "http://api.cctvpoldalampung.com/ApiCctv/";
	//public static String base_URL = "http://api.feb-ubl.com/Api_Siater/v2/";
	//Default configuration for WAMP - 80 is default port for WAMP and 10.0.2.2 is localhost IP in Android Emulator
	// Server user login url
	public static String URL = base_URL+"Api_Siater/";

	public static String listMhs = "http://192.168.43.244:10/Api_Siater/include/View_Absen_Mahasiswa.php";

	//params in API
	public static String TAG = "tag";
	public static String TAG_LOGIN = "login";
	public static String username = "username";
	public static String password = "password";
}