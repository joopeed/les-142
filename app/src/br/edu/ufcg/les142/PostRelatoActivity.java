package br.edu.ufcg.les142;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import br.edu.ufcg.les142.models.Comentario;
import br.edu.ufcg.les142.models.Relato;
import br.edu.ufcg.les142.models.StatusRelato;
import com.parse.*;

import java.util.ArrayList;

/**
 * Created by Rodr on 25/11/2014.
 */
public class PostRelatoActivity extends Activity {

    // UI references.
    private EditText postEditText;
    private Button attachButton;
    private Button postButton;
    private Bitmap image;
    private Relato post;
    private ParseGeoPoint geoPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitypostrelato);
        Intent intent = getIntent();
        Location location = intent.getParcelableExtra(Application.INTENT_EXTRA_LOCATION);
        geoPoint = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
        postEditText = (EditText) findViewById(R.id.post_edittext);
        attachButton = (Button) findViewById(R.id.attach_button);
        postButton = (Button) findViewById(R.id.post_button);
        post = new Relato();
        postButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                post();
            }
        });
        attachButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        //      updatePostButtonState();
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            image = (Bitmap) extras.get("data");
            post.setImage(image);

            // mImageView.setImageBitmap(imageBitmap);
        }
    }

    public void post() {
        String text = postEditText.getText().toString().trim();
       /* // Set up a progress dialog
        final ProgressDialog dialog = new ProgressDialog(PostRelatoActivity.this);
        dialog.setMessage(getString(R.string.relato));
        dialog.show();
*/
        // Create a post.
        // Set the location to the current user's location
        final ProgressDialog dialog = new ProgressDialog(PostRelatoActivity.this);
        dialog.setMessage(getString(R.string.progress_posting));
        dialog.show();
        post.setLocalizacao(geoPoint);
        post.setDescricao(text);
        post.setStatusRelato(StatusRelato.ABERTO);
        post.setUser(ParseUser.getCurrentUser());
        post.setComentarios(new ArrayList< Comentario>());

        ParseACL acl = new ParseACL();
        // Give public read access
        acl.setPublicReadAccess(true);
        post.setACL(acl);

        // Save the post
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                finish();
            }
        });
    }
}
