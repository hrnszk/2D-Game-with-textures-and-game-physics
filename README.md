# 2D-Game-with-textures-and-game-physics

This program is for Homework 1.3: 2D Game with textures and game physics




Completed: 

# 1. Avatar control #

=================

2 fast 2 furious

----------------

An avatar object can be accelerated forward and backwards by holding down keys on the keyboard. Two other keys apply torques turning the avatar clockwise or counter-clockwise. Different dissipation factors affect the forwards and sideways velocity components of the avatar. The result should be a vehicle that barely slides sideways and appears to turn when both accelerated forwards and rotated to one side.




# 3. Texture animation #

====================

Boom

----

When a projectile hits an enemy, or the avatar collides with something (or anything that makes sense in your game) they explode. An animated explosion is displayed using a semi-transparent quad, its texture including sprites of all movement phases, and the vertex shader adjusting texture coordinates to show the proper phase, received as a uniform parameter.




# 4. Parenting #

============

Flames

------

When some force is applied to the avatar, exhaust flames appear at appropriate locations (i.e. where the thrusters are). The flames have fixed locations in the frame of reference fixed to the avatar, and they are shown or hidden depending on whether the forces are active.






Implemented:

# 2. Collisions #

=============

Assume all objects to be disc-like, or static horizontal capsules, for the purposes of collisions, unless otherwise noted. Collision between a disc and a static capsule can be detected by first deciding if the center of the disc over/under the flat part of the platform. Then, it is either a disc vs. line or a disc vs. disc collision.



Flipper

-------

There should be some horizontal 2D capsules for the avatar to collide with. The platforms are elastic, making the avatar bounce back from them. There may even be bouncers with a restitution coefficient higher than one. Otherwise, collision response should be physically correct (i.e. impulse acts along the collision normal).




# 6. AI  #

=====

Path animation

--------------

There should be some enemies moving along parametric curves (perhaps a heart, an egg, or a quadrifolium). They should always turn so that they move ahead. Compute curve formula derivatives symbolically (www.wolframalpha.com).





Reference: 
László Szécsi. "Homework 1.3: 2D Game with textures and game physics". _CORA (Community of Online Research Assignments)_. 2021. Web. Sunday, 7 November 2021.
