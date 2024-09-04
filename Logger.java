class Logger {
    public static void error(Exception e, boolean loud) {
        e.printStackTrace();
    }
    public static void error(Exception e) {
        error(e, false);
    }
}