package com.example.kalk;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)` | number
            //        | functionName `(` expression `)` | functionName factor
            //        | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return +parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    if (!eat(')')) throw new RuntimeException("Missing ')'");
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    if (eat('(')) {
                        x = parseExpression();
                        if (!eat(')')) throw new RuntimeException("Missing ')' after argument to " + func);
                    } else {
                        x = parseFactor();
                    }
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn0 = findViewById(R.id.bt0);
        Button btn1 = findViewById(R.id.bt1);
        Button btn2 = findViewById(R.id.bt2);
        Button btn3 = findViewById(R.id.bt3);
        Button btn4 = findViewById(R.id.bt4);
        Button btn5 = findViewById(R.id.bt5);
        Button btn6 = findViewById(R.id.bt6);
        Button btn7 = findViewById(R.id.bt7);
        Button btn8 = findViewById(R.id.bt8);
        Button btn9 = findViewById(R.id.bt9);
        Button div = findViewById(R.id.divide);
        Button mul = findViewById(R.id.multiply);
        Button add = findViewById(R.id.add);
        Button sub = findViewById(R.id.substract);
        Button eq = findViewById(R.id.eq);
        Button c = findViewById(R.id.clear);
        TextView text = findViewById(R.id.textview);
        TextView error = findViewById(R.id.error);


        btn1.setBackgroundColor(getResources().getColor(R.color.black));
        btn1.setTextColor(getResources().getColor(R.color.white));

        btn2.setBackgroundColor(getResources().getColor(R.color.black));
        btn2.setTextColor(getResources().getColor(R.color.white));

        btn3.setBackgroundColor(getResources().getColor(R.color.black));
        btn3.setTextColor(getResources().getColor(R.color.white));

        btn4.setBackgroundColor(getResources().getColor(R.color.black));
        btn4.setTextColor(getResources().getColor(R.color.white));

        btn5.setBackgroundColor(getResources().getColor(R.color.black));
        btn5.setTextColor(getResources().getColor(R.color.white));

        btn6.setBackgroundColor(getResources().getColor(R.color.black));
        btn6.setTextColor(getResources().getColor(R.color.white));

        btn7.setBackgroundColor(getResources().getColor(R.color.black));
        btn7.setTextColor(getResources().getColor(R.color.white));

        btn8.setBackgroundColor(getResources().getColor(R.color.black));
        btn8.setTextColor(getResources().getColor(R.color.white));

        btn9.setBackgroundColor(getResources().getColor(R.color.black));
        btn9.setTextColor(getResources().getColor(R.color.white));

        btn0.setBackgroundColor(getResources().getColor(R.color.black));
        btn0.setTextColor(getResources().getColor(R.color.white));

        sub.setBackgroundColor(getResources().getColor(R.color.black));
        sub.setTextColor(getResources().getColor(R.color.white));

        div.setBackgroundColor(getResources().getColor(R.color.black));
        div.setTextColor(getResources().getColor(R.color.white));

        add.setBackgroundColor(getResources().getColor(R.color.black));
        add.setTextColor(getResources().getColor(R.color.white));

        mul.setBackgroundColor(getResources().getColor(R.color.black));
        mul.setTextColor(getResources().getColor(R.color.white));

        eq.setBackgroundColor(getResources().getColor(R.color.black));
        eq.setTextColor(getResources().getColor(R.color.white));

        c.setBackgroundColor(getResources().getColor(R.color.black));
        c.setTextColor(getResources().getColor(R.color.red));

        btn0.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                text.append("0");
            }

        });


        c.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                text.setText("");
            }

        });
        eq.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                String textData = text.getText().toString();
                try {
                    text.setText(String.valueOf(eval((textData))));
                    error.setText("");
                    String regex1 = "\\b\\d+\\s*/\\s*0(\\.0*)?\\b";
                    Pattern pattern = Pattern.compile(regex1);

                    for (int i = 0; i < 1; i++) {
                        String testCase = textData;
                        Matcher matcher = pattern.matcher(testCase);
                        if (matcher.find()) {
                            error.setText("nie mozna dzielic przez 0");
                            text.setText("");
                        }
                    }
                }
                catch (Exception e){
                        error.setText("error");
                }
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                text.append("1");
                error.setText("");
            }

        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                text.append("2");
                error.setText("");
            }

        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                text.append("3");
                error.setText("");
            }

        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                text.append("4");
                error.setText("");
            }

        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                text.append("5");
                error.setText("");
            }

        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                text.append("6");
                error.setText("");
            }

        });
        btn7.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                text.append("7");
                error.setText("");
            }

        });
        btn8.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                text.append("8");
                error.setText("");
            }

        });
        btn9.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                text.append("9");
                error.setText("");
            }

        });
        div.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                text.append("/");
                error.setText("");
            }

        });
        mul.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                text.append("*");
                error.setText("");
            }

        });
        add.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                text.append("+");
                error.setText("");
            }

        });
        sub.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                text.append("-");
                error.setText("");
            }

        });

    }
}