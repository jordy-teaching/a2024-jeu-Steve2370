package Z_Fighter;

import java.awt.*;

public class Animation {
    private Image[] frames;
    private int currentFrameIndex;
    private long lastFrameTime;
    private int frameDelay;
    private boolean loop;
    private boolean isFinished;

    public Animation(Image[] frames, int frameDelay) {
        this.frames = frames;
        this.frameDelay = frameDelay;
        this.currentFrameIndex = 0;
        this.lastFrameTime = System.currentTimeMillis();
        this.loop = true;
        this.isFinished = false;
    }

    public Image getCurrentFrame() {
        return frames[currentFrameIndex];
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public void reset() {
        this.currentFrameIndex = 0;
        this.isFinished = false;
        this.lastFrameTime = System.currentTimeMillis();
    }

    public Image[] getFrames() {
        return frames;
    }

    public void update() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastFrameTime >= frameDelay) {
            currentFrameIndex++;

            if (currentFrameIndex >= frames.length) {

                if (loop) {
                    currentFrameIndex = 0;
                } else {
                    currentFrameIndex = frames.length - 1;
                    isFinished = true;
                }
            }
            lastFrameTime = currentTime;
        }
    }

    public int getFrameCount() {
        return frames.length;
    }
}
