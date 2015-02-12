package net.validcat.fishing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class FishingList extends Activity {
	private EditText etPlace, etDate;
	private Button btnCreate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fishing_list);

		// находим элементы
		etPlace = (EditText) findViewById(R.id.etPlace);
		etDate = (EditText) findViewById(R.id.etDate);
		btnCreate = (Button) findViewById(R.id.btnCreate);

		// прописываем обработчик для кнопки
		btnCreate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// cобираем информацию для отправки
				String place = getPlaceText();
				String date = getDateText();

				// отправляем информацию
				Intent data = new Intent();
				ToDoItem.packageIntent(data, place, date);

				// отправляем контейнер
				setResult(RESULT_OK, data);
				finish();

			}

		});

	}

	// считываем введенный пользователем текст
	private String getPlaceText() {
		return etPlace.getText().toString();
	}

	// считываем введенный пользователем текст
	private String getDateText() {
		return etDate.getText().toString();
	}

}
