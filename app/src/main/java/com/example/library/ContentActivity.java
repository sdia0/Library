package com.example.library;

import android.Manifest;
import android.graphics.Color;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class ContentActivity extends AppCompatActivity {
    TextView tId;
    EditText etTitle, etAuthor, etYear, etDescription;
    String selectedGenre;
    Spinner spGenre;
    ImageView ivCover;
    ImageButton btnDelete;
    Button btnSave;
    Uri imagePath = null;
    DbHelper db = new DbHelper(this);
    private final int GALLERY_REQ_CODE = 1;
    Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_content);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tId = findViewById(R.id.tId);
        etTitle = findViewById(R.id.etTitle);
        etAuthor = findViewById(R.id.etAuthor);
        etYear = findViewById(R.id.etYear);
        etDescription = findViewById(R.id.etDecsription);

        spGenre = findViewById(R.id.spGenre);

        ivCover = findViewById(R.id.ivCover);
        btnDelete = findViewById(R.id.btnDelete);
        btnSave = findViewById(R.id.btnInsert);

        book = (Book) getIntent().getSerializableExtra("key");
        tId.setText("ID: " + book.getId());

        etTitle.setText(book.getTitle());
        etAuthor.setText(book.getAuthor());
        etYear.setText(book.getYear());
        etDescription.setText(book.getDescription());

        // Создаем массив данных для Spinner
        List<String> spinnerItems = db.getGenres();

        // Создаем адаптер для Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGenre.setAdapter(adapter);

        // Устанавливаем слушатель для Spinner
        spGenre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedGenre = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Действие, если ничего не выбрано
            }
        });

        try {
            imagePath = Uri.parse(book.getCover());
            ivCover.setImageURI(imagePath);
        } catch (Exception e) {
            requestPermission();
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Book newBook = new Book(
                        etTitle.getText().toString(),
                        etAuthor.getText().toString(),
                        etYear.getText().toString(),
                        selectedGenre,
                        imagePath.toString(),
                        etDescription.getText().toString());
                newBook.setId(book.getId());

                try {
                    Boolean result = db.updateData(newBook);
                    if (result) {
                        Toast.makeText(getApplicationContext(),
                                "Data updated",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    } else Toast.makeText(getApplicationContext(),
                            "Cannot update data",
                            Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),
                            e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    Log.i("keykey",newBook.getId()+"");
                    Log.i("keykey",e.getMessage());
                }
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Boolean checkInsert = db.deleteData(book);
                    if (checkInsert == true) {
                        Toast.makeText(getApplicationContext(),
                                "Data deleted",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    } else
                        Toast.makeText(getApplicationContext(),
                                "Data not deleted",
                                Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),
                            e.toString(),
                            Toast.LENGTH_SHORT).show();

                    Log.i("keykey",book.getId()+"");
                    Log.i("keykey",e.getMessage());
                }
            }
        });

        ivCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //image load
                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery, GALLERY_REQ_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_REQ_CODE) {
                //for gallery
                imagePath = data.getData();
                ivCover.setImageURI(data.getData());
                ivCover.setBackgroundColor(Color.TRANSPARENT);
                ivCover.setPadding(0, 0, 0, 0);
            }
        }
    }

    private static final int PERMISSION_REQUEST_CODE = 100;

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.READ_MEDIA_IMAGES
            }, PERMISSION_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE);
        }
    }
}