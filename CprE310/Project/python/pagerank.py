import string
import math
import logging

class Node:
	def __init__(self, rank):
		self.items_incoming = []
		self.items_outgoing = []
		self.rank = []
		self.setRank(rank)
		
	def setRank(self, rank):
		self.rank.append(rank)
	
	def getRank(self, iteration):
		return self.rank[iteration]
		
	def getCurrentRank(self):
		return self.rank[-1]
	
	def getIncomingLinks(self):
		return self.items_incoming
		
	def getOutgoingLinkCount(self):
		return len(self.items_outgoing)
		
	def addIncomingLink(self, name):
		self.items_incoming.append(name)
	
	def addOutgoingLink(self, name):
		self.items_outgoing.append(name)
		
class Pair:
	def __init__(self, val1, val2):
		self.key = val1
		self.value = val2
		
class PageRank:
	def __init__(self, pVal, stopDiff, initialRank):
		self.pVal = pVal
		self.stopDiff = stopDiff
		self.initialRank = initialRank
		self.nodes = []
		
	def main(self, data):
		strArr = string.split(data)
		
		for i in range(0, len(strArr) - 1, 2):
			fromVal  = str(strArr[i])
			i += 1
			toVal = str(strArr[i])
			
			# Add the current node
			found = False
			for j in self.nodes:
				if j.key == fromVal:
					j.value.addOutgoingLink(toVal)
					found = True
					break
			if not found:
				nNode = Node(self.initialRank)
				nNode.addOutgoingLink(toVal)
				nPair = Pair(fromVal, nNode)
				self.nodes.append(nPair)
				
			# Add the destination node
			found = False
			for j in self.nodes:
				if j.key == toVal:
					j.value.addIncomingLink(fromVal)
					found = True
					break
			if not found:
				nNode = Node(self.initialRank)
				nNode.addIncomingLink(fromVal)
				nPair = Pair(toVal, nNode)
				self.nodes.append(nPair)
				
		self.calculateRank()
		
		return self.nodes
	
	def calculateRank(self):
		iteration = 0
		prevDiff = 10000
		
		while prevDiff > self.stopDiff:
			prevDiff = 0
			for val in self.nodes:
				summed = 0				
				
				for tempLink in val.value.getIncomingLinks():
					for findLink in self.nodes:
						if findLink.key == tempLink:
							summed += (findLink.value.getRank(iteration) / findLink.value.getOutgoingLinkCount())
							break

				newRank = (1.0 - self.pVal) + (summed * self.pVal)
				
				if prevDiff < math.fabs(val.value.getCurrentRank() - newRank):
					prevDiff = math.fabs(val.value.getCurrentRank() - newRank)
				
				val.value.setRank(newRank)
			
			iteration += 1
