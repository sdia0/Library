package com.example.library;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class AddActivity extends AppCompatActivity {
    EditText etTitle, etAuthor, etYear, etDescription;
    String selectedGenre;
    Spinner spGenre;
    ImageView ivCover;
    Button btnInsert;
    Uri imagePath = null;
    DbHelper db;
    private final int GALLERY_REQUEST_CODE=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etTitle = findViewById(R.id.etTitle);
        etAuthor = findViewById(R.id.etAuthor);
        etYear = findViewById(R.id.etYear);
        etDescription = findViewById(R.id.etDecsription);

        spGenre = findViewById(R.id.spGenre);

        ivCover = findViewById(R.id.ivCover);
        btnInsert = findViewById(R.id.btnInsert);

        db = new DbHelper(this);

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

        ivCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery,GALLERY_REQUEST_CODE);
            }
        });

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etTitle.getText().toString();
                String author = etAuthor.getText().toString();
                String year = etYear.getText().toString();
                String description = etDescription.getText().toString();

                if (title.isEmpty() || author.isEmpty() || year.isEmpty() || description.isEmpty()) {
                    Toast.makeText(getApplicationContext(),
                        "Заполните все поля!",
                        Toast.LENGTH_LONG).show();
                    return;
                }

                Book book = new Book(title, author, year, imagePath.toString(), selectedGenre, description);
                boolean result = db.insertData(book);

                if (result) {
                    Toast.makeText(getApplicationContext(),
                        "Data inserted",
                        Toast.LENGTH_SHORT);
                    finish();
                }
                else Toast.makeText(getApplicationContext(),
                            "Data not inserted",
                            Toast.LENGTH_SHORT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            if (requestCode==GALLERY_REQUEST_CODE){
                imagePath = data.getData();
                ivCover.setBackgroundColor(Color.TRANSPARENT);
                ivCover.setPadding(0, 0, 0, 0);
                ivCover.setImageURI(imagePath);
            }
        }
    }
}