package es.uv.jorge.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import es.uv.jorge.R;

public class MunicipalityViewHolder extends RecyclerView.ViewHolder{
    private CardView cardViewMunicipality,cardViewTestPCR, cardViewCumulativeIncidence;
    private ConstraintLayout contraintLayoutWrapper;
    private ImageView imageViewMunicipality,imageViewTestPCR,imageViewCumulativeIncidence;
    private TextView textViewMunicipality, textViewTestPCR, textViewCumulativeIncidence;
    public MunicipalityViewHolder(@NonNull View itemView) {
        super(itemView);
        contraintLayoutWrapper = itemView.findViewById(R.id.contraintLayoutWrapper);

        cardViewMunicipality = itemView.findViewById(R.id.cardViewMunicipality);
        cardViewTestPCR = itemView.findViewById(R.id.cardViewTestPCR);
        cardViewCumulativeIncidence = itemView.findViewById(R.id.cardViewCumulativeIncidence);

        imageViewMunicipality = itemView.findViewById(R.id.imageViewMunicipality);
        imageViewTestPCR = itemView.findViewById(R.id.imageViewTestPCR);
        imageViewCumulativeIncidence = itemView.findViewById(R.id.imageViewCumulativeIncidence);

        textViewMunicipality = itemView.findViewById(R.id.textViewValueMunicipality);
        textViewTestPCR = itemView.findViewById(R.id.textViewValueTestPCR);
        textViewCumulativeIncidence = itemView.findViewById(R.id.textViewValueCumulativeIncidence);
    }

    public ConstraintLayout getContraintLayoutWrapper() {
        return contraintLayoutWrapper;
    }

    public CardView getCardViewMunicipality() {
        return cardViewMunicipality;
    }

    public CardView getCardViewTestPCR() {
        return cardViewTestPCR;
    }

    public CardView getCardViewCumulativeIncidence() {
        return cardViewCumulativeIncidence;
    }

    public ImageView getImageViewMunicipality() {
        return imageViewMunicipality;
    }

    public ImageView getImageViewTestPCR() {
        return imageViewTestPCR;
    }

    public ImageView getImageViewCumulativeIncidence() {
        return imageViewCumulativeIncidence;
    }

    public TextView getTextViewMunicipality() {
        return textViewMunicipality;
    }

    public TextView getTextViewTestPCR() {
        return textViewTestPCR;
    }

    public TextView getTextViewCumulativeIncidence() {
        return textViewCumulativeIncidence;
    }
}
