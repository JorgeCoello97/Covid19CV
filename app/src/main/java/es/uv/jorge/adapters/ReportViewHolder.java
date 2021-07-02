package es.uv.jorge.adapters;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

import es.uv.jorge.R;

public class ReportViewHolder extends RecyclerView.ViewHolder {
    private ImageView imageView;
    private LinearLayout linearLayout;
    private TextInputLayout textInputLayout_id, textInputLayout_stardate, textInputLayout_num_symptoms;
    private EditText editTextID, editTextStartDate, editTextNumSymptoms;
    public ReportViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.imageView_report);
        linearLayout = itemView.findViewById(R.id.linearLayout_report);
        textInputLayout_id = itemView.findViewById(R.id.textInputLayout_id_report);
        textInputLayout_stardate = itemView.findViewById(R.id.textInputLayout_startdate_report);
        textInputLayout_num_symptoms = itemView.findViewById(R.id.textInputLayout_symptoms_report);

        editTextID = textInputLayout_id.getEditText();
        editTextStartDate = textInputLayout_stardate.getEditText();
        editTextNumSymptoms = textInputLayout_num_symptoms.getEditText();
    }

    public ImageView getImageView() {
        return imageView;
    }

    public LinearLayout getLinearLayout() {
        return linearLayout;
    }

    public EditText getEditTextID() {
        return editTextID;
    }

    public EditText getEditTextStartDate() {
        return editTextStartDate;
    }

    public EditText getEditTextNumSymptoms() {
        return editTextNumSymptoms;
    }
}
