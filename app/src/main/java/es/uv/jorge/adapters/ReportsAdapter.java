package es.uv.jorge.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import es.uv.jorge.DetailsReportActivity;
import es.uv.jorge.R;
import es.uv.jorge.constants.Constants;
import es.uv.jorge.constants.Flags;
import es.uv.jorge.model.Report;

public class ReportsAdapter extends RecyclerView.Adapter<ReportViewHolder>
{
    private CursorAdapter cursorAdapter;
    private Context context;
    private FragmentActivity activity;
    private ReportViewHolder holder;

    public ReportsAdapter(Context context, Cursor cursor, String[] municipalitiesNames, FragmentActivity activity) {
        this.context = context;
        this.activity = activity;

        this.cursorAdapter = new CursorAdapter(context, cursor,0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_report_covid, parent, false);
                return view;
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                Report report = new Report(cursor);

                int reportID = report.getId();
                String startdate = report.getStartDate();
                int numSymptoms = report.getTotalNumSymptoms();

                holder.getEditTextID().setText(reportID+"");
                holder.getEditTextStartDate().setText(startdate);
                holder.getEditTextNumSymptoms().setText(numSymptoms+"");

                if (numSymptoms > 10){
                    holder.getImageView().setImageResource(R.drawable.dearths);
                }else {
                    holder.getImageView().setImageResource(R.drawable.issue14);
                }

                holder.getLinearLayout().setOnClickListener(v -> {
                    Intent intent = new Intent(context, DetailsReportActivity.class);

                    Gson gson = new Gson();
                    String sListMunicipalitiesNames = gson.toJson(municipalitiesNames);
                    intent.putExtra(Constants.EXTRA_LIST_MUNICIPALITIES_NAMES, sListMunicipalitiesNames);

                    intent.putExtra(Constants.EXTRA_REPORT_ID, reportID);

                    activity.startActivityForResult(intent, Flags.DetailsMunicipalityActivity.REQUEST_FROM_DETAILS_MUNICIPALITY_ACTIVITY);
                });
            }
        };
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = cursorAdapter.newView(context, cursorAdapter.getCursor(), parent);
        holder = new ReportViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        cursorAdapter.getCursor().moveToPosition(position);
        cursorAdapter.bindView(holder.itemView, context, cursorAdapter.getCursor());
    }

    @Override
    public int getItemCount() {
        return cursorAdapter.getCount();
    }
}
