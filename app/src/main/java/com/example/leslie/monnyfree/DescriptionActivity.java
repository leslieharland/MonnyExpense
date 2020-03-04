package com.example.leslie.monnyfree;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.leslie.monnyfree.common.MonnyBaseFragmentActivity;
import com.example.leslie.monnyfree.common.transactions.EditTransactionCommonFunctions;
import com.example.leslie.monnyfree.core.RequestCodes;
import com.example.leslie.monnyfree.model.Expense;
import com.example.leslie.monnyfree.utils.BitmapUtil;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Leslie on 3/24/2018.
 */

public class DescriptionActivity extends MonnyBaseFragmentActivity implements IPickResult{

    private static final String TAG = DescriptionActivity.class.getSimpleName();
    @BindView(R.id.edtDescription)
    EditText edtDescription;
    @BindView(R.id.btnAddPhoto)
    Button btnAddPhoto;

    private Toolbar mToolbar;
    private EditTransactionCommonFunctions mCommon;

    @BindView(R.id.ivImage)
    ImageView ivImage;

    //region Add photo declarations
    @OnClick(R.id.btnAddPhoto)
    public void launchImageIntent() {
      /*  Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), RequestCodes.PICK_IMAGE);*/

        PickImageDialog.build(new PickSetup()
        .setSystemDialog(true)
        ).show(this);
    }

    @OnClick(R.id.btnSave)
    public void onClick(){
        saveDescription();
    }

    @BindView(R.id.btnRemoveImage)
    ImageButton btnRemoveImage;
    @OnClick(R.id.btnRemoveImage)
    public void removeImage(){
        btnRemoveImage.setVisibility(View.GONE);
        ivImage.setImageDrawable(null);
        image = null;
    }
    //endregion
    String image;
    String description;

    private static Expense mExpense;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_description);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        mCommon = new EditTransactionCommonFunctions();

        initializeToolbar();
        initializeModel();


        mExpense = new Expense();
        if (savedInstanceState != null) {
            description = savedInstanceState.getString(Expense.DESCRIPTION);
            Log.d(TAG, "initializeModel: " + "savedInstancedState" + description);
            edtDescription.setText(description);
            ivImage.setImageBitmap(BitmapUtil.convertToBitmap(savedInstanceState.getString(Expense.IMAGE)));
        }
    }

    private void initializeModel() {
        Log.d(TAG, "initializeModel: ");
        if (getIntent().getExtras() != null) {

            description = getIntent().getStringExtra(Expense.DESCRIPTION);
            image = getIntent().getStringExtra(Expense.IMAGE);
            Log.d(TAG, "initializeModel: " + "description:" + description + "image:" + image);
            if (description != null) {
                edtDescription.setText(description);
                edtDescription.setSelection(description.length());
            }
            if (image != null) {
                if (!TextUtils.isEmpty(String.valueOf(image))) {
                    Bitmap bm = BitmapUtil.convertToBitmap(image);
                    showImage(bm);
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Expense.DESCRIPTION, String.valueOf(edtDescription.getText()));
        outState.putString(Expense.IMAGE, String.valueOf(image));


    }

    public void initializeToolbar(){
        setDisplayHomeAsUpEnabled(true);
        TextView toolbarText = findViewById(R.id.toolbar_title);
        toolbarText.setText(R.string.description);
    }

    public void saveDescription() {
        String description = edtDescription.getText().toString();
        sendResultToActivity(description, image);
    }

    private void sendResultToActivity(String description, String image) {
        Intent result = new Intent();
        result.putExtra(Expense.DESCRIPTION, description);
        result.putExtra(Expense.IMAGE, image);
        setResult(Activity.RESULT_OK, result);
        finish();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: " + requestCode);
        switch (requestCode) {
            case (RequestCodes.CROP_IMAGE):
                if (resultCode == Activity.RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap bm = extras.getParcelable("data");
                    showImage(bm);

                    image = BitmapUtil.convertToBase64(bm);
                    //Log.d(TAG, "Image: " + image);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        finish();

    }

    private void showImage(Bitmap bm){
        btnRemoveImage.setVisibility(View.VISIBLE);
        ivImage.setVisibility(View.VISIBLE);
        ivImage.setImageBitmap(bm);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                setResult(Activity.RESULT_CANCELED);
                finishActivity(RequestCodes.DESCRIPTION);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onPickResult(PickResult r) {
        if (r.getError() == null) {
            mCommon.launchImageCropIntent(r.getUri(), this);


        } else {
            //Handle possible errors
            //TODO: do what you have to do with r.getError();
        }
    }
}
