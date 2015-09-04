package tp1.client;
import util.RESTClient;
import util.Test;


public class Main {

	public static void main(String[] args) {
		String what = RESTClient.get("http://www.mocky.io/v2/55e9d2ca65ec0623180125e7", Test.class).toString();
		System.out.println(what);
	}

}
