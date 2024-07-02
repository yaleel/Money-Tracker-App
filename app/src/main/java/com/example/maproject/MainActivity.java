package com.example.maproject;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
//////////////////////////////
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Date;
import java.text.SimpleDateFormat;
////////////////////////////////

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /////////////////////////////////////////
        //initialize value with saved amount

        TextView textView3 = findViewById(R.id.textView3);
        String content = readFromFile("file.txt");
        double total = 0;
        try {
            total = totalCal(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        textView3.setText(""+total);

        //categories + amounts
        ScrollView textView9 = findViewById(R.id.textView9);
        String allContents = strFormat(content);
        TextView textView10 = findViewById(R.id.textView10);
        textView10.setText(allContents);

        //date
        Date currentDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM, yyyy");
        String formattedDate = formatter.format(currentDate);
        TextView textView2 = findViewById(R.id.textView2);
        textView2.setText(formattedDate);

        //main page button
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Activity2.class);
                startActivity(intent);
            }
        });


        Button deleteBtn = findViewById(R.id.deleteBtn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFile("file.txt");
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });


        ///////////////////////////////////////////////////////////
    }

    public String readFromFile(String fileName) {
        File path = getApplicationContext().getFilesDir();
        File readFrom = new File(path, fileName);
        byte[] content = new byte[(int) readFrom.length()];
        try {
            FileInputStream stream = new FileInputStream(readFrom);
            stream.read(content);
            return new String(content);

        } catch (Exception e) {
            return "";
        }

    }

    public double totalCal(String fileContents) throws IOException {
        double totalAmount = 0.0;
        StringReader stringReader = new StringReader(fileContents);
        BufferedReader bufferedReader = new BufferedReader(stringReader);
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(",");
                for (String part : parts) {
                    if (part.trim().startsWith("Amount:")) {
                        String amountStr = part.trim().substring("Amount:".length()).trim();
                        totalAmount += Double.parseDouble(amountStr);
                    }
                }
            }
        } finally {
            bufferedReader.close();
        }
        return totalAmount;
    }

    public String strFormat(String content) {
        StringBuilder formattedContent = new StringBuilder();

        String[] lines = content.split("\n");

        for (String line : lines) {
            if (line.contains("Category:")) {

                String categoryName = line.substring(line.indexOf("Category:") + 10, line.indexOf(",")).trim();
                String amount = line.substring(line.indexOf("Amount:") + 8).trim();
                //formattedContent.append(String.format("%-40s", categoryName)).append("\n"); // Add a newline here
                //formattedContent.append(String.format("%-20s", amount)).append("\n\n");
                formattedContent.append(String.format("%-20s", "Category:")).append(String.format("%-20s", categoryName)).append(String.format("%-21s", "\nAmount:")).append(String.format("%-20s", amount)).append("\n\n");


            }
        }

        return formattedContent.toString();
    }


}