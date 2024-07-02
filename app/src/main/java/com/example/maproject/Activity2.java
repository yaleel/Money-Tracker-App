package com.example.maproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

public class Activity2 extends AppCompatActivity {

    double amount;
    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //back button
        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity2.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //add button
        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextNumberDecimal = findViewById(R.id.editTextNumberDecimal);
                String amountStr = editTextNumberDecimal.getText().toString();
                if (amountStr.isEmpty()) {
                    editTextNumberDecimal.setError("Amount cannot be empty");
                    return;
                }
                amount = Double.parseDouble(amountStr);

                EditText editTextText = findViewById(R.id.editTextText);
                String category = editTextText.getText().toString();
                if (category.isEmpty()) {
                    editTextText.setError("Category cannot be empty");
                    return;
                }

                writeToFile("file.txt", category, amount);

                Intent intent = new Intent(Activity2.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void writeToFile(String fileName, String category, double amount) {
        File path = getApplicationContext().getFilesDir();
        File file = new File(path, fileName);
        try {
            FileOutputStream writer = new FileOutputStream(file, true);

            int lastIndex = 0;
            if (file.length() > 0) {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String lastLine = "";
                String currentLine;
                while ((currentLine = br.readLine()) != null) {
                    lastLine = currentLine;
                }
                br.close();

                lastIndex = Integer.parseInt(lastLine.split(":")[0].trim()) + 1;
            }
            String line = lastIndex + ": Category: " + category + " , Amount: " + amount + "\n";
            writer.write(line.getBytes());
            writer.close();
            Toast.makeText(getApplicationContext(), "Changes were saved", Toast.LENGTH_SHORT).show();

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String fileContents;
            while ((fileContents = reader.readLine()) != null) {
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
