package zkrtdrone.zkrt.com.jackmvpmoudle.base;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import zkrtdrone.zkrt.com.jackmvpmoudle.util.InstanceUtil;

/**
 * Created by jack_xie on 2016/12/17.
 */

public abstract class BaseDialogMvpFragment<P extends BasePresenter, M extends BaseModel> extends DialogFragment{
    protected P mPresenter;
    private Unbinder unbinder;
    public Activity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity) context;
        BaseApplication.activity = mActivity;
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mPresenter = InstanceUtil.getInstance(this, 0);
        if(this instanceof BaseView){
            mPresenter.setVM(this, InstanceUtil.getInstance(this, 1));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(getFragmentViewId(), null);
        unbinder = ButterKnife.bind(this, view);
        getViewFindByid(view);
        return view;
    }

    protected void getViewFindByid(View view){}

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        init(savedInstanceState);
    }

    /**
     * Call for onCreateView
     *
     * @return View for Fragment
     */
    protected abstract int getFragmentViewId();

    protected abstract void init(Bundle savedInstanceState);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //unbinder.unbind();
    }
}
