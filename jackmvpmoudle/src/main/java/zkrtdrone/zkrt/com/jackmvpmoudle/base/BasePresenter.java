package zkrtdrone.zkrt.com.jackmvpmoudle.base;

import dji.midware.base.RxManager;

/**
 * Use mvp, to Use this class
 *
 */
public abstract class BasePresenter<M, V>
{
    public M mModel;
    public V mView;
    public RxManager rxManager = new RxManager();
    public void setVM(V v, M m)
    {
        this.mView = v;
        this.mModel = m;
        this.onAttached();
    }

    public abstract void onAttached();

    public void onDetached()
    {
        rxManager.clear();
    }

}
