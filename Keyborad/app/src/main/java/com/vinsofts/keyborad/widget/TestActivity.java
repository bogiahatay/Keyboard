package com.vinsofts.keyborad.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.vinsofts.keyborad.R;
import com.vinsofts.keyborad.base.BaseActivity;
import com.vinsofts.keyborad.listener.IOnClickItem;
import com.vinsofts.keyborad.utils.DeviceUtils;
import com.vinsofts.keyborad.utils.MLog;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestActivity extends BaseActivity {
    public static final int SPAN_COUNT = 10;
    @BindView(R.id.rcv_caro)
    RecyclerView rcv;
    private Caro[][] listCaro = new Caro[SPAN_COUNT][SPAN_COUNT];
    private Human human1;
    private Human human2;
    private CaroAdapter adapter;
    private boolean isHuman1 = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        human1 = new Human();
        human1.id = 1;
        human1.idImg = R.drawable.ic_o;
        human1.name = "Human O";

        human2 = new Human();
        human2.id = 2;
        human2.idImg = R.drawable.ic_x;
        human2.name = "Human X";

        rcv.setHasFixedSize(true);
        rcv.setLayoutManager(new GridLayoutManager(mActivity, SPAN_COUNT));
        adapter = new CaroAdapter(mActivity, getListCaro(), new IOnClickItem() {
            @Override
            public void onClickItem(Object obj, int position) {
                Caro caro = (Caro) obj;
                if (caro.check) {
                    MLog.e("check");
                } else {
                    MLog.e("un check");
                    if (isHuman1) {
                        caro.id = human1.id;
                        caro.idImg = human1.idImg;
                    } else {
                        caro.id = human2.id;
                        caro.idImg = human2.idImg;
                    }
                    isHuman1 = !isHuman1;
                    caro.check = true;
                    adapter.notifyItemChanged(position);
                    check(caro, position);
                }
            }
        });
        rcv.setAdapter(adapter);
    }

    private void check(Caro caro, int position) {
        int id = caro.id;
        int number = 1;
        int row = position / TestActivity.SPAN_COUNT;
        int column = position % TestActivity.SPAN_COUNT;

        if (number + getLeft(id, row, column) + getRight(id, row, column) >= 4) {
            if (caro.id == human1.id) {
                Toast.makeText(mActivity, human1.name + " win !!!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mActivity, human2.name + " win !!!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        if (number + getBottom(id, row, column) + getTop(id, row, column) >= 4) {
            if (caro.id == human1.id) {
                Toast.makeText(mActivity, human1.name + " win !!!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mActivity, human2.name + " win !!!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        if (number + getLeftTop(id, row, column) + getRightBottom(id, row, column) >= 4) {
            if (caro.id == human1.id) {
                Toast.makeText(mActivity, human1.name + " win !!!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mActivity, human2.name + " win !!!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        if (number + getLeftBottom(id, row, column) + getRightTop(id, row, column) >= 4) {
            if (caro.id == human1.id) {
                Toast.makeText(mActivity, human1.name + " win !!!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mActivity, human2.name + " win !!!", Toast.LENGTH_SHORT).show();
            }
            return;
        }


    }

    private int getRight(int id, int row, int column) {
        int retur = 0;
        try {
            for (int i = 1; i < SPAN_COUNT - column; i++) {
                Caro caro = listCaro[row][column + i];
                if (caro.check && caro.id == id) {
                    retur += 1;
                } else {
                    return retur;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return retur;
        }
        return retur;
    }

    private int getLeft(int id, int row, int column) {
        int retur = 0;
        try {
            for (int i = 1; i <= column; i++) {
                Caro caro = listCaro[row][column - i];
                if (caro.check && caro.id == id) {
                    retur += 1;
                } else {
                    return retur;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return retur;
        }
        return retur;
    }

    private int getBottom(int id, int row, int column) {
        int retur = 0;
        try {
            for (int i = 1; i < SPAN_COUNT - row; i++) {
                Caro caro = listCaro[row + i][column];
                if (caro.check && caro.id == id) {
                    retur += 1;
                } else {
                    return retur;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return retur;
        }
        return retur;
    }

    private int getTop(int id, int row, int column) {
        int retur = 0;
        try {
            for (int i = 1; i <= row; i++) {
                Caro caro = listCaro[row - i][column];
                if (caro.check && caro.id == id) {
                    retur += 1;
                } else {
                    return retur;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return retur;
        }
        return retur;
    }

    private int getLeftTop(int id, int row, int column) {
        int retur = 0;
        try {
            for (int i = 1; i <= SPAN_COUNT; i++) {
                Caro caro = listCaro[row - i][column - i];
                if (caro.check && caro.id == id) {
                    retur += 1;
                } else {
                    return retur;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return retur;
        }
        return retur;
    }

    private int getRightBottom(int id, int row, int column) {
        int retur = 0;
        try {
            for (int i = 1; i <= SPAN_COUNT; i++) {
                Caro caro = listCaro[row + i][column + i];
                if (caro.check && caro.id == id) {
                    retur += 1;
                } else {
                    return retur;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return retur;
        }
        return retur;
    }

    private int getLeftBottom(int id, int row, int column) {
        int retur = 0;
        try {
            for (int i = 1; i <= SPAN_COUNT; i++) {
                Caro caro = listCaro[row + i][column - i];
                if (caro.check && caro.id == id) {
                    retur += 1;
                } else {
                    return retur;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return retur;
        }
        return retur;
    }

    private int getRightTop(int id, int row, int column) {
        int retur = 0;
        try {
            for (int i = 1; i <= SPAN_COUNT; i++) {
                Caro caro = listCaro[row - i][column + i];
                if (caro.check && caro.id == id) {
                    retur += 1;
                } else {
                    return retur;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return retur;
        }
        return retur;
    }

    public Caro[][] getListCaro() {
        for (int i = 0; i < listCaro.length; i++) {
            for (int j = 0; j < listCaro[i].length; j++) {
                listCaro[i][j] = new Caro();
            }
        }
        return listCaro;
    }


}

class CaroAdapter extends RecyclerView.Adapter<CaroAdapter.ViewHolder> {

    private IOnClickItem onClickItem;
    private Context context;
    Caro[][] list;

    public CaroAdapter(Context context, Caro[][] list, IOnClickItem onClickItem) {
        this.list = list;
        this.context = context;
        this.onClickItem = onClickItem;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_caro, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Caro caro = list[position / TestActivity.SPAN_COUNT][position % TestActivity.SPAN_COUNT];
        if (caro.check) {
            holder.imvTick.setImageResource(caro.idImg);
        } else {
            holder.imvTick.setImageBitmap(null);
        }
    }

    @Override
    public int getItemCount() {
        return list.length * list.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imv_tick)
        ImageView imvTick;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.getLayoutParams().width = DeviceUtils.getScreenWidth((Activity) context) / TestActivity.SPAN_COUNT;
            itemView.getLayoutParams().height = DeviceUtils.getScreenWidth((Activity) context) / TestActivity.SPAN_COUNT;
            itemView.setOnClickListener(v -> onClickItem.onClickItem(list[getAdapterPosition() / TestActivity.SPAN_COUNT][getAdapterPosition() % TestActivity.SPAN_COUNT], getAdapterPosition()));
        }
    }
}

class Human {
    public int id;
    public int idImg;
    public String name;
}

class Caro {
    public int id;
    public boolean check = false;
    public int idImg;
}

