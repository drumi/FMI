import Data.Char
import Data.List

calcLuhnChecksum :: Int -> Int
calcLuhnChecksum n = mod (10 - (mod(sum lst4) 10)) 10
 where 
  lst1 = [((ord x) - (ord '0'))| x <-(show n)]
  lst2 = map (\x -> if (even x) then 2 else 1) [1,2..]
  lst3 = zipWith (*) lst1 lst2
  lst4 = map (\x -> if(x>9) then x-9 else x) lst3

------------------------------------------------------------------------------------------

data BTree = Empty | Node Int BTree BTree

getNode:: BTree -> Int
getNode Empty = -1
getNode (Node num l r) = num

treeInfo::BTree -> [(Int,Int,Int,Int)] --[(Node, left child, right child, depth)]
treeInfo (Empty) = []
treeInfo (Node num l r) = helper (Node num l r) 0
 where
  helper :: BTree ->Int-> [(Int,Int,Int,Int)]
  helper (Empty) depth = []
  helper (Node num l r) depth = [(num,(getNode l),(getNode r),depth)] ++ (helper l (depth + 1)) ++ (helper r (depth + 1))

printBT::BTree -> [String]
printBT (Empty) = []
printBT (Node num l r) = const 0
 where
  info = treeInfo (Node num l r)
  maxDepth = head [d|(_,_,_,d)<-info, all (\(a,b,c,e)->d>=e) info]
  const :: Int->[String]
  const depth 
   |depth > maxDepth = []
   |otherwise = (concat ["_"++(show a)++"_"| (a,b,c,d)<-info, d == depth]:[]) ++ const (depth + 1)
  

bts :: BTree
bts = Node 0 (Node 1 (Node 3 (Node 7 Empty Empty) Empty) (Node 4 (Node 8 Empty Empty) Empty))(Node 2 (Node 5 (Node 9 Empty Empty)(Node 10 Empty Empty))(Node 6 Empty(Node 11 Empty Empty)))

