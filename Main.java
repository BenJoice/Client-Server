import java.math.BigInteger;

public class Main {
	/* public static BigInteger binPow(BigInteger a, BigInteger b, BigInteger m) {
	        a %= m;
	        if (b == 0) return 1;
	        else if (b % 2 == 0) {
	            return binPow((a.multiply(a)).remainder(m), b.divide(BigInteger.valueOf(2)), m);
	        }
	        else return (a.multiply( binPow(a, b.subtract(BigInteger.valueOf(1)), m))).remainder( m);
	    }
	    */
	public static void main(String[] args) {
			BigInteger N=BigInteger.valueOf(2048);
			String text="Text";
		 	BigInteger q=Utils.getPrime(1024);
		 	BigInteger p=Utils.getPrime(1024);
		 	BigInteger n=q.multiply(p);
		 	BigInteger fi=q.subtract(BigInteger.valueOf(1)).multiply(p.subtract(BigInteger.valueOf(1)));
		 	BigInteger e=BigInteger.valueOf(65537);
		 	BigInteger d;
		 	BigInteger k=BigInteger.valueOf(2);
		 	for(;;) {
		 		if(k.multiply(fi).add(BigInteger.valueOf(1)).remainder(e).equals(BigInteger.valueOf(0))){
		 			d=k.multiply(fi).add(BigInteger.valueOf(1)).divide(e);
		 			break;
		 		}
		 		else {
		 			k=k.add(BigInteger.valueOf(1));
		 		}
		  	}
		 	BigInteger bits =new BigInteger(text.getBytes());
		 	System.out.println(bits.toString());
		 	bits=bits.modPow(e, n);
		 	System.out.println(bits.toString());
		 	bits=bits.modPow(d, n);
		 	System.out.println(bits.toString());
	}

}
