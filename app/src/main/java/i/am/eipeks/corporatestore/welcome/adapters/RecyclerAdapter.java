package i.am.eipeks.corporatestore.welcome.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import i.am.eipeks.corporatestore.R;
import i.am.eipeks.corporatestore.welcome.classes.CorporateItem;
import i.am.eipeks.corporatestore.welcome.database.MyDatabaseHelper;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.Holder> {

    private Context context;
    private ArrayList<CorporateItem> corporateItems, cartItem;
    private LayoutInflater inflater;
    private SQLiteDatabase sqLiteDatabase;

    public RecyclerAdapter(Context context, ArrayList<CorporateItem> corporateItems, LayoutInflater inflater){
        this.corporateItems = corporateItems;
        this.context = context;
        this.inflater = inflater;
        MyDatabaseHelper itemsInCart = new MyDatabaseHelper(context);
        sqLiteDatabase = itemsInCart.getWritableDatabase();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) inflater.inflate(R.layout.card_view_layout, parent, false);
        return new Holder(cardView);
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        final CorporateItem currentItem = corporateItems.get(position);
        holder.productName.setText(String.format("%s: %s", "Name", currentItem.getName()));
        holder.quantity.setText(String.format("%s: %s", "Quantity", currentItem.getQuantity()));
        holder.size.setText(String.format("%s: %s", "Size", currentItem.getSize()));
        holder.type.setText(String.format("%s: %s", "Type", currentItem.getType()));
        holder.color.setText(String.format("%s: %s", "Color", currentItem.getColor()));
        holder.dateReceived.setText(String.format("%s: %s", "Date", currentItem.getDateReceived()));
        holder.popup.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        PopupMenu popupMenu = new PopupMenu(context, holder.popup);
                        popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                int id = item.getItemId();
                                switch (id){
                                    case R.id.add_to_cart:
                                        if (Integer.parseInt(currentItem.getQuantity()) < 1){
                                            Snackbar.make(v, "Out of stock", Snackbar.LENGTH_LONG).show();
                                        } else {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                            builder.setTitle(context.getResources().getString(R.string.price_hint));

                                            View priceLayout = LayoutInflater.from(context)
                                                    .inflate(R.layout.price, null);
                                            TextInputLayout inputLayout = (TextInputLayout) priceLayout.findViewById(R.id.price_text_input_layout);
                                            inputLayout.setHint(context.getResources().getString(R.string.price_hint));

                                            builder.setView(priceLayout);
                                            builder.setPositiveButton("DONE", null);

                                            final EditText editText = (EditText) priceLayout.findViewById(R.id.price_edit_text);

                                            AlertDialog priceDialog = builder.create();
                                            priceDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                                                @Override
                                                public void onShow(final DialogInterface dialogInterface) {
                                                    Button button = ((AlertDialog) dialogInterface).getButton(AlertDialog.BUTTON_POSITIVE);
                                                    button.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            if (TextUtils.isEmpty(editText.getText())){
                                                                Snackbar.make(v, "Field is empty", Snackbar.LENGTH_LONG).show();
                                                            } else {
                                                                currentItem.setPrice(editText.getText().toString());
                                                                ContentValues contentValues = new ContentValues();
                                                                contentValues.put(MyDatabaseHelper.NAME_COLUMN, currentItem.getName());
                                                                contentValues.put(MyDatabaseHelper.TYPE_COLUMN, currentItem.getType());
                                                                contentValues.put(MyDatabaseHelper.SIZE_COLUMN, currentItem.getSize());
                                                                contentValues.put(MyDatabaseHelper.COLOR_COLUMN, currentItem.getColor());
                                                                contentValues.put(MyDatabaseHelper.PRICE_COLUMN, currentItem.getPrice());

                                                                sqLiteDatabase.insert(MyDatabaseHelper.CART_TABLE_NAME, null, contentValues);

                                                                Snackbar.make(v, "Done", Snackbar.LENGTH_LONG)
                                                                        .setAction("UNDO", new View.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(View view) {
//                                                                            sqLiteDatabase.delete(MyDatabaseHelper.T)
                                                                            }
                                                                        }).show();
                                                                dialogInterface.dismiss();
                                                            }
                                                        }
                                                    });
                                                }
                                            });
                                        }
//                                        Snackbar.make(v, "Clicked", Snackbar.LENGTH_SHORT).show();
                                }
                                return false;
                            }
                        });
                        popupMenu.show();
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return corporateItems.size();
    }

    static class Holder extends RecyclerView.ViewHolder{

        TextView productName, type, size, quantity, color, dateReceived;
        ImageView icon;
        ImageButton popup;

        private Holder(CardView itemView) {
            super(itemView);
            productName = (TextView) itemView.findViewById(R.id.card_view_product_name);
            type = (TextView) itemView.findViewById(R.id.card_view_type);
            size = (TextView) itemView.findViewById(R.id.card_view_size);
            quantity = (TextView) itemView.findViewById(R.id.card_view_quantity);
            color = (TextView) itemView.findViewById(R.id.card_view_color);
            dateReceived = (TextView) itemView.findViewById(R.id.card_view_date_received);

            icon = (ImageView) itemView.findViewById(R.id.card_view_icon);

            popup = (ImageButton) itemView.findViewById(R.id.popup_button);
        }
    }

}
