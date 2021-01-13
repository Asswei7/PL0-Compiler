# -*- coding: utf-8 -*-
"""
Created on Thu Dec 31 16:35:53 2020

@author: Asswei
"""

import sys
 
def func(a,b):
	return (a+b)

if __name__ == '__main__':
	a = []
	#print(a)
	#print("Hello")
	for i in range(1, len(sys.argv)):
		a.append((int(sys.argv[i])))

	print(func(a[0],a[1]))