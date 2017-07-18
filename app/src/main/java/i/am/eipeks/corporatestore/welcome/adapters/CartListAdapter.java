package i.am.eipeks.corporatestore.welcome.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import i.am.eipeks.corporatestore.R;
import i.am.eipeks.corporatestore.welcome.classes.CorporateItem;


public class CartListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<CorporateItem> corporateItems;

    public CartListAdapter(Context context, ArrayList<CorporateItem> corporateItems){
        this.context = context;
        this.corporateItems = corporateItems;
    }

    @Override
    public int getCount() {
        return corporateItems.size();
    }

    @Override
    public CorporateItem getItem(int position) {
        return corporateItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        CorporateItem currentItem = corporateItems.get(position);

        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.cart_items_layout, parent, false);
        }

        TextView productName = (TextView) convertView.findViewById(R.id.cart_items_product_name);
        TextView price = (TextView) convertView.findViewById(R.id.cart_items_price);
        TextView size = (TextView) convertView.findViewById(R.id.cart_items_size);
        TextView type = (TextView) convertView.findViewById(R.id.cart_items_type);
        TextView color = (TextView) convertView.findViewById(R.id.cart_items_color);

        productName.setText(currentItem.getName());
        price.setText(currentItem.getPrice());
        size.setText(currentItem.getSize());
        type.setText(currentItem.getType());
        color.setText(currentItem.getColor());

        return convertView;
    }
}
