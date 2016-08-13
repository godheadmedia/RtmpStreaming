package ghm.rtmp.example;

import android.app.ProgressDialog;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;


public class MainActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {

    private String url;
    private VideoView mVideoView;
    private TextView empty;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initialise Vitamio Library.
        Vitamio.isInitialized(this);

        //setting full screen windows
        this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //stream url
        url = getResources().getString(R.string.streamurl);

        empty = (TextView) this.findViewById(R.id.empty);
        mVideoView = (VideoView) this.findViewById(R.id.surface_view);
        mVideoView.setMediaController(new MediaController(this));//to enable media controllers replace "new MediaController(this)" with null
        mVideoView.setOnCompletionListener(this);
        mVideoView.setOnPreparedListener(this);
        mVideoView.setOnErrorListener(this);// setting on error listner

        Uri videoUri = Uri.parse(url);
        mVideoView.setVideoURI(videoUri);

        mVideoView.requestFocus();
        loading();

    }

    private void loading() {

        pd = ProgressDialog.show(MainActivity.this, "", "Nothing");
        pd.getWindow().setLayout(WindowManager.LayoutParams.FILL_PARENT,
                WindowManager.LayoutParams.FILL_PARENT);
        pd.setContentView(R.layout.progress);
        empty.setVisibility(View.GONE);
    }

    private void loadComplete(MediaPlayer arg0) {
        pd.dismiss();
        empty.setVisibility(View.GONE);
        mVideoView.start();
        mVideoView.resume();
    }

    private void error(String msg) {
        mVideoView.setVisibility(View.GONE);
        empty.setVisibility(View.VISIBLE);
        if (msg != null)
            empty.setText(msg);
    }

    @Override
    public void onPrepared(MediaPlayer arg0) {
        Log.d("GHM", "Prepared");
        loadComplete(arg0);

    }

    @Override
    public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
        //error Playing the stream
        pd.dismiss();
        empty.setText("Unable to play this channel.");
        Log.d("GHM", "Error");
        error("Unable to play this channel.");
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer arg0) {
        Log.d("GHM", "Complete");
        // error("Streaming Complete");
    }
}
