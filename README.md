# TrainSet
This is a work in progress. 

The Trainset is a Hornby OO system on a board 2.4m x 1.2m. The design will be implemented in stages and the final layout will come later. 

This code is for the control unit. There are multiple components; 

## Power Board 
The Power board is the provider of power to the control boards. It provides are set of buses: 

* A-Bus: 12v 6Amp. To power the trains. 
* B-Bus: 5v to provide power to the Odroids 
* C-Bus: 12v 1.2Amp. to control pulse switching the points. 

## Control boards 

These are A4 sized electronics boards. They each control one function aspect. They are managed by an onboard Odroid Xu4 Single Board controller. This has multiple GPIO pins which switch at 1.2v. This is too low to power most electronic components so they utilize a Shifter Shield. 

The Shifter Shield allows the GPIO pins to be switched at 5v and provides the pins in Raspberry Pi notation. This makes using pi4J easier. 

The control boards each run a single application. This is a purpose built Java block, which uses pi4j. 

### Sensor Board

The sensors detect train positions. The position uses micro reed relays embedded in the track. A magnet is mounted beneath the train to toggle the relay. This feeds back to a series of inputs on the Odroid. Each one is a Digital Input signal and has a listener to detect a state change. The state is then passed to the Fat Controller. 

The sensor board supports 18 discreet sensors. It also provides +5v and Ground signals. 

### Motor Board 

The Motor Board provides 12V dc current to track sections. The segments are all isolated, so transitioning from section to section requires care. 

The board supports 4 power controllers. These are L298N Motor/Stepper drivers. Each one has two poer outputs, hence 8 sections can be controlled. 

The Odroid does not have embedded PWM (unlike the Pi), but this can be simulated in software. A cycle time of 100 milliseconds is the default. In each cycle the current will be either on or off for part of the cycle. This has the effect at the DC end of providing an average mean power voltage. 

The Motor board controller supports acceleration and deceleration, so that the speed can build from a start (or reduce to stop) in discreet stages. 

### Points Boards 

Points are tricky. They require switching from the thru (ie, straight ahead direction) to the branch direction. The motor requires a pulse in either direction to achieve this. Hence you have a pulse to switch each directions. This means having two relays per point. 

The track design allows for switching between sections. This is performed using a pair of points which are either both in the thru direction or bot in the banchn direction. Hence, we double up these points on to one pair of relays. 

The board (currently) supports a block of 16 relays, hence managing 8 points (or point pairs). The limiutation here is the number of GPIOs available on the Odroid (28). 

We could, in theory, add an 8 channel relay block, thus increasing the point density to 12. The problem is one of physical space and construction. 

There is nothing stopping having multiple points boards. 

## Fat Controller 

This (with the tongue firmly in the cheek) is a nod to Thomas the Tank Engine. The Fat Controller is the brains that issues instructions to Points and Motor boards and listens for train detection from the Sensor Board. 

This is very definitely a work in progress. 


