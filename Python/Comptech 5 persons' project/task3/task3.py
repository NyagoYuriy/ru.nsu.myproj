from keras.layers import Input, Dense, Convolution1D, Convolution2D, Dropout, Flatten
from keras.models import Model, Sequential
import pickle
import numpy

PATH = 'horizons-master/data/horizons.pickle'
INTERNAL_1 = 64
INTERNAL_2 = 64
DROPOUT_1 = 0.125
DROPOUT_2 = 0.125
INTERNAL_ACTIVATION = 'relu'
EXIT_ACTIVATION = 'linear'
OPTIMIZER = 'rmsprop'
LOSS_FUNCTION = 'mean_squared_error'



with open(PATH, 'rb') as handle:
        raw_data = pickle.load(handle)
data = raw_data[0]
max_x = data['xn']+data['dx']
max_y = data['yn']+data['dy']
min_x = data['x0']
min_y = data['y0']
nx = data['nx'];
ny = data['ny'];
print("Max x: "+str(max_x)+", min_x: "+str(min_x)+"\n")
print("Max y: "+str(max_y)+", min_y: "+str(min_y)+"\n")
print("Count x:"+str(nx)+", count y:"+str(ny))


model = Sequential()
model.add(Dense(INTERNAL_1, activation=INTERNAL_ACTIVATION, input_shape=(2,), input_dtype='float32'))
model.add(Dropout(DROPOUT_1))
model.add(Dense(INTERNAL_2, activation=INTERNAL_ACTIVATION))
model.add(Dropout(DROPOUT_2))
#hard_sigmoid = 180 / 165
#sigmoid = 182 / 160
#relu = 142 / 165
#tanh = 146 / 164 
model.add(Dense(1, activation=EXIT_ACTIVATION)) 

model.compile(optimizer=OPTIMIZER, loss=LOSS_FUNCTION, metrics=['accuracy']) #mean_squared_error 
model.summary();# adam # rmsprop #sgd
#sigmoid
#sgd = /161
#adam = /193
#rmsprop = /184/179/181//177/183

#sigmod, rmsprop (configuration)
in_arr = [];
out_arr = [];

max_z = 0
for x in range(nx):
	for y in range(ny):
		if (max_z<data['z'][x][y]):
			max_z=data['z'][x][y]
print("Max_z: "+str(max_z))



in_arr = []
out_arr = []

for x in range(nx):
	for y in range(ny):
		cur_x = data['x'][x][y]
		cur_y = data['y'][x][y]
		cur_z = data['z'][x][y]	
		if (numpy.isnan(cur_z) or numpy.isnan(cur_y) or numpy.isnan(cur_x)):
			print("One coords is NaN: x: "+str(x)+", y: "+str(y))
		else:
			in_arr+=[[cur_x/max_x, cur_y/max_y]]
			out_arr+=[[cur_z/max_z]]
model.fit(numpy.asarray(in_arr), numpy.asarray(out_arr), verbose=1, epochs=50) #186	

#model.fit(numpy.asarray([[0.12,0.12]]), numpy.asarray([0.123]), verbose=0, epochs=20)
#model.fit(numpy.asarray([[0.12,0.13],[0.13,0.12]]), numpy.asarray([0.123, 0.124]), verbose=1, epochs=20)
#model.fit(numpy.asarray([[0.12,0.13]]), numpy.asarray([0.124]), verbose=1, epochs=20)


print("F(0, 0): "+str(data['z'][0][0])+"\n")
print("F(50, 0): "+str(data['z'][1][0])+"\n")
print("F(0, 50): "+str(data['z'][0][1])+"\n")
print("F(50, 50): "+str(data['z'][1][1])+"\n")
print("Test1: "+str(model.predict(numpy.asarray([[0,0]])))+", test2: ")
print(out_arr[0])
print("\n")
print("After calculation: \n")
print("F(25,25): "+str(model.predict(numpy.asarray([[25/max_x,25/max_y]]), verbose=1)*max_z)+"\n")
print("F(0,0): "+str(model.predict(numpy.asarray([[0/max_x,0/max_y]]), verbose=1)*max_z)+"\n")
print("F(50,0): "+str(model.predict(numpy.asarray([[50/max_x,0/max_y]]), verbose=1)*max_z)+"\n")
print("F(0,50): "+str(model.predict(numpy.asarray([[0/max_x,50/max_y]]), verbose=1)*max_z)+"\n")
print("F(50,50): "+str(model.predict(numpy.asarray([[50/max_x,50/max_y]]), verbose=1)*max_z)+"\n")
print("\n\n")

print("F(1200, 0): "+str(data['z'][24][0])+"\n")
print("F(1250, 0): "+str(data['z'][25][0])+"\n")
print("F(1200, 50): "+str(data['z'][24][1])+"\n")
print("F(1250, 50): "+str(data['z'][25][1])+"\n")
print("Test1: "+str(model.predict(numpy.asarray([[24,0]])))+", test2: ")
print(out_arr[24])
print("\n")
print("After calculation: \n")
print("F(1225,25): "+str(model.predict(numpy.asarray([[1225/max_x,25/max_y]]), verbose=1)*max_z)+"\n")
print("F(1200,0): "+str(model.predict(numpy.asarray([[1200/max_x,0/max_y]]), verbose=1)*max_z)+"\n")
print("F(1250,0): "+str(model.predict(numpy.asarray([[1250/max_x,0/max_y]]), verbose=1)*max_z)+"\n")
print("F(1200,50): "+str(model.predict(numpy.asarray([[1200/max_x,50/max_y]]), verbose=1)*max_z)+"\n")
print("F(1250,50): "+str(model.predict(numpy.asarray([[1250/max_x,50/max_y]]), verbose=1)*max_z)+"\n")