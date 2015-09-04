public final class Chronometre{
    private long debut, fin;
 
    public void demarer(){
    	debut = System.currentTimeMillis();
    }
 
    public void arreter(){
    	fin = System.currentTimeMillis();
    }
 
    public long getTime() {
        return fin-debut;
    }
 
    public long getMilliseconds() {
        return fin-debut;
    }
 
    public double getSeconds() {
        return (fin - debut) / 1000.0;
    }
 
    public double getMinutes() {
        return (fin - debut) / 60000.0;
    }
 
    public double getHours() {
        return (fin - debut) / 3600000.0;
    }
}