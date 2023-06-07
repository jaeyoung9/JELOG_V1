package jelog.server.main.Controller;


import java.io.File;

public class BaseController {
    static class request{
        public String ID;
        public result result;
        public payload payload;
    }

    public static class result{
        public String Status;
        public String Message;
        public String Request_Time;
        public String Token;
    }
    public static class payload{
        public String Body;
        public File[] File;
    }
}
