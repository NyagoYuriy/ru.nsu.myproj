from keras.layers import Input, Dense, Dropout, Embedding, LSTM
from keras.models import Model, Sequential
import numpy as np
import pylab as plt

#stateful -?
LAYER1_NEUROS = 64
LAYER2_NEUROS = 64
DROPOUT = 0.25
EXIT_ACTIVATION = 'softmax'#'sigmoid'
LOSS_FUNCTION= 'binary_crossentropy'
OPTIMIZER = 'sgd'
TIME_STEPS = 50 #NONE - any length sequence
PARAMS = 1
INTERNAL_ACTIVATION = 'tanh'
DELTA_TIME = 0.02
FIT_DATASETS = 2000
TEST_DATASETS = 20
EPOCHS = 25 #5 #1

model = Sequential()
model.add(LSTM(LAYER1_NEUROS, activation=INTERNAL_ACTIVATION, input_shape=(TIME_STEPS,PARAMS), input_dtype='float32', return_sequences=True))
model.add(LSTM(LAYER2_NEUROS, activation=INTERNAL_ACTIVATION))
model.add(Dense(1, activation=EXIT_ACTIVATION))
#model.add(Dropout(DROPOUT))
#model.add(Dense(1))
model.compile(loss=LOSS_FUNCTION, optimizer=OPTIMIZER, metrics=['accuracy'])
model.summary()

def plot_sdata(x, true=None, pred=None, nr=10, rand=False):
    nr = np.min([x.shape[0], nr])
    if rand:
        recs = np.random.randint(0, x.shape[0], nr)
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

def _berlage(t, t0, f, g):
    w = np.sin(2*f*np.pi*(t-t0)) 
    h = np.exp(-g*(t-t0)**2) 
    s = w * h
    s[t<t0] = 0
    return s

def generate_surprise_data_set(nr, ns, 
                      dt=1,
                      t0_min=.25,
                      t0_max=.8,
                      g_min=2,
                      g_max=4,
                      f_max=.3,
                      f_min=.1,
                      n_min=.01,
                      n_max=.2,
                      seis_let=_berlage, 
					  do_randomize = 1):   
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

    flag = []
    for k,v in enumerate(x):
        if ((np.random.sample()>=0.5) and (do_randomize)):
            x[k]-=x[k]
            flag += [0]
        else: flag += [1]
    x = x + noise
    jy = t0
   
    return x, flag #jy

print("Generate training dataset\n")
x, flags = generate_surprise_data_set(FIT_DATASETS, TIME_STEPS, dt=DELTA_TIME)
x-=x.min()
if (x.max()>0):
	x/=x.max()
	
new_x = []
for row in x:
	temp_row = []
	for item in row:
		temp_row+=[[item]]
	new_x+=[temp_row]
x = np.asarray(new_x)

new_flags = []
for flag in flags:
	new_flags+=[[flag]]
flags = np.asarray(new_flags)

print(x, flags)

#model.fit()
plot_sdata(x)

model.fit(x, flags, verbose=1, epochs = EPOCHS)

print("Generate test dataset\n")
x, flags = generate_surprise_data_set(TEST_DATASETS, TIME_STEPS, dt=DELTA_TIME, do_randomize=1)

new_x = []
for row in x:
	temp_row = []
	for item in row:
		temp_row+=[[item]]
	new_x+=[temp_row]
x = np.asarray(new_x)

results = model.predict(x, verbose=1)
print("Neuro answer:\n")
print(results)
print("True answers:\n")
print(flags)

#model.fit(np.asarray([[[0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.1,0.2,0.5,0.7]]]), np.asarray([[1]]), verbose=1, epochs=1)
#model.fit(np.asarray([[[0],[0],[0],[0.4],[0.5],[0.6],[0.7],[0.1],[0.2],[0.5],[0.7]]]), np.asarray([[1]]), verbose=1, epochs=1)

#print(model.predict(np.asarray([[[0.7],[0.6],[0.5],[0.4],[0.5],[0.2],[0.1],[0.1],[0.2],[0.5],[0.7]]]), verbose=1))