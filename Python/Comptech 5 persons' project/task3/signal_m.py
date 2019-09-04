import pandas as pd
import numpy as np
import pylab as plt

def plot_sdata(x, true=None, pred=None, nr=10, rand=False):
    nr = np.min([x.shape[0], nr])
    if rand:
        recs = np.random.randint(0, x.shape[0], nr)
#         recs = np.arange(0,nr)
    else:
        recs = np.arange(nr)
    x = x[recs, :]
    if true is not None:
        true = true[recs, :]
        
    if pred is not None:
        pred = pred[recs, :]
        
    for i, s in enumerate(x):
        plt.plot(s + i, 'k')
        
        if true is not None:
            j = true[i, :].argmax() -1
            plt.plot(j, s[j] + i, 'sr')
        
        if pred is not None:
            j = pred[i, :].argmax() -1
            plt.plot(j, s[j] + i, '^b')
        
    plt.show()
    
def plot_sdata_jy(x, true=None, pred=None, nr=10, rand=False):
    nr = np.min([x.shape[0], nr])
    if rand:
        recs = np.random.randint(0, x.shape[0], nr)
    else:
        recs = np.arange(nr)
    x = x[recs, :]
    if true is not None:
        true = true[recs]
        
    if pred is not None:
        pred = pred[recs]
        
    for i, s in enumerate(x):
        plt.plot(s + i, 'k')
        
        if true is not None:
            j = true[i].astype(np.int32)
            plt.plot(j, s[j] + i, 'sr', ms=15)
        
        if pred is not None:
            j = pred[i].astype(np.int32)
            plt.plot(j, s[j] + i, '^b', ms=15)
        
    plt.show()

def save_experiment(model,
                    x_train_lstm,
                    y_train,
                    x_test_lstm,
                    y_test,
                    path):
    import os
    import pickle
    
    model.save(os.path.join(path, '120118_model.h5'))
    model.save_weights(os.path.join(path, '120118_weights.h5'))
    pickle.dump({"x_train_lstm": x_train_lstm, 
             "y_train": y_train, 
             "x_test_lstm": x_test_lstm, 
             "y_test":y_test}, 
            open( os.path.join(path, "120118_data.p"), "wb" ))
    

def _berlage(t, t0, f, g):
    w = np.sin(2*f*np.pi*(t-t0)) 
    h = np.exp(-g*(t-t0)**2)
    
    s = w * h
    s[t<t0] = 0
    return s

def generate_data_set(nr, ns, 
                      dt=1,
                      t0_min=.25,
                      t0_max=.8,
                      g_min=2,
                      g_max=4,
                      f_max=.3,
                      f_min=.1,
                      n_min=.01,
                      n_max=.2,
                      seis_let=_berlage,):
    
    fn = 1/2/dt
    n = np.random.uniform(n_min, n_max, nr)[np.newaxis].T
    
    t0 = dt * np.random.uniform((ns-1)*t0_min, (ns-1)*t0_max, nr)[np.newaxis].T
    f = np.random.uniform(fn*f_min, fn*f_max, nr)[np.newaxis].T
    al = np.random.randint(g_min, g_max+1, nr)[np.newaxis].T
    g = f/al
    
    noise = np.random.randn(nr, ns) * n
    t = np.arange(0, ns*dt, dt)
    t = np.repeat(t[np.newaxis], nr, axis=0)
    x = _berlage(t, t0, f,g)
    x = x + noise
    jy = t0
    
    return x, jy

import numpy as np
from matplotlib import pyplot as plt
from IPython.display import clear_output
