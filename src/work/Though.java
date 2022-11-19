package work;

import arc.*;
import arc.Files.*;
import arc.backend.sdl.*;
import arc.util.*;

import static arc.backend.sdl.jni.SDL.*;

public class Though extends ApplicationCore{
    public static void main(String[] args){
        try{
            new SdlApplication(new Though(), new SdlConfig(){{
                title = "Do They Work Though?";
                width = height = 1;
                resizable = decorated = false;
                disableAudio = true;
                initialVisible = false;
                vSyncEnabled = false;
            }});
        }catch(Throwable err){
            Log.err(Strings.neatError(Strings.getFinalCause(err)));
            SDL_ShowSimpleMessageBox(SDL_MESSAGEBOX_ERROR, "Crash!", Strings.getFinalMessage(err));
        }
    }

    @Override
    public void setup(){
        Core.app.exit();
    }
}
