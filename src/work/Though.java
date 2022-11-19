package work;

import arc.*;
import arc.Files.*;
import arc.backend.sdl.*;
import arc.graphics.*;
import arc.graphics.gl.*;
import arc.graphics.g2d.*;
import arc.util.*;

import static arc.Core.*;
import static arc.backend.sdl.jni.SDL.*;

public class Though extends ApplicationCore{
    public static void main(String[] args){
        try{
            new SdlApplication(new Though(), new SdlConfig(){{
                title = "Do they work though?";
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
        atlas = TextureAtlas.blankAtlas();
        batch = new SortedSpriteBatch();
        camera = new Camera();

        context(this::butDo, this::theyWork);

        Log.infoTag("App", "Like, obviously.");
        Core.app.exit();
    }

    public void context(Runnable... runs){
        for(Runnable run : runs){
            Draw.sort(true);

            run.run();

            Draw.reset();
            Draw.flush();
            Draw.sort(false);
        }
    }

    public FrameBuffer create(int w, int h){
        FrameBuffer buffer = new FrameBuffer(w, h);
        buffer.begin(Color.clear);

        camera.position.set(w / 2f - 0.5f, h / 2f - 0.5f);
        camera.width = w;
        camera.height = h;
        camera.update();

        Draw.proj(camera);
        return buffer;
    }

    public void save(FrameBuffer buffer, String name){
        Draw.flush();
        Pixmap pix = ScreenUtils.getFrameBufferPixmap(0, 0, buffer.getWidth(), buffer.getHeight());

        buffer.end();
        buffer.dispose();

        PixmapIO.writePng(files.local(name + ".png"), pix);
        pix.dispose();
    }

    protected void butDo(){
        FrameBuffer screen = create(160, 160);
        Fill.quad(
            0f, 0f, Color.red.toFloatBits(),
            160f, 0f, Color.green.toFloatBits(),
            160f, 160f, Color.white.toFloatBits(),
            0f, 160f, Color.blue.toFloatBits()
        );

        save(screen, "1-of");
    }

    protected void theyWork(){
        FrameBuffer screen = create(17 * 8, 15 * 8);
        byte[] pixels = {
            0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0,
            0, 0, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 0, 0,
            0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0,
            0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0,
            1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1,
            1, 2, 2, 1, 1, 1, 2, 2, 2, 2, 2, 1, 1, 1, 2, 2, 1,
            1, 2, 2, 1, 1, 1, 2, 2, 2, 2, 2, 1, 1, 1, 2, 2, 1,
            1, 2, 2, 1, 1, 1, 2, 2, 1, 2, 2, 1, 1, 1, 2, 2, 1,
            0, 1, 2, 2, 2, 2, 2, 1, 1, 1, 2, 2, 2, 2, 2, 1, 0,
            1, 1, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 2, 1, 1,
            1, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1,
            1, 2, 2, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 2, 2, 1,
            0, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 0,
            0, 0, 0, 1, 1, 2, 2, 2, 2, 2, 2, 2, 1, 1, 0, 0, 0,
            0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0
        };

        for(int y = 0; y < 15; y++){
            for(int x = 0; x < 17; x++){
                Draw.color(switch(pixels[x + y * 17]){
                    case 0 -> Color.clear;
                    case 1 -> Color.black;
                    default -> Color.white;
                });

                Draw.rect(atlas.white(), x * 8f + 4f, y * 8f + 4f, 8f, 8f);
            }
        }

        save(screen, "2-course");
    }
}
