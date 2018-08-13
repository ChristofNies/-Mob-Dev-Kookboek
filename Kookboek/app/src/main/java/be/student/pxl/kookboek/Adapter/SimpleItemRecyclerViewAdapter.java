package be.student.pxl.kookboek.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import be.student.pxl.kookboek.Entities.Recipe;
import be.student.pxl.kookboek.R;
import be.student.pxl.kookboek.RecipeDetailActivity;
import be.student.pxl.kookboek.RecipeDetailFragment;
import be.student.pxl.kookboek.RecipeListActivity;

public class SimpleItemRecyclerViewAdapter
        extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

    private final RecipeListActivity mParentActivity;
    private final List<Recipe> mValues;
    private final boolean mTwoPane;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Recipe recipe = (Recipe) view.getTag();
            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putSerializable("recipeId", recipe.getId());
                RecipeDetailFragment fragment = new RecipeDetailFragment();
                fragment.setArguments(arguments);
                mParentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.recipe_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, RecipeDetailActivity.class);
                intent.putExtra("recipeId", recipe.getId());

                context.startActivity(intent);
            }
        }
    };

    public SimpleItemRecyclerViewAdapter(RecipeListActivity parent,
                                         List<Recipe> recipeList,
                                         boolean twoPane) {
        mValues = recipeList;
        mParentActivity = parent;
        mTwoPane = twoPane;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mTitleView.setText(mValues.get(position).getTitle());
        Bitmap bitmap = BitmapFactory.decodeByteArray(mValues.get(position).getPicture(), 0, mValues.get(position).getPicture().length);
        holder.mImageView.setImageBitmap(bitmap);

        holder.itemView.setTag(mValues.get(position));
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mTitleView;
        final ImageView mImageView;

        ViewHolder(View view) {
            super(view);
            mTitleView = (TextView) view.findViewById(R.id.recipe_title);
            mImageView = (ImageView) view.findViewById(R.id.recipe_photo);
        }
    }
}