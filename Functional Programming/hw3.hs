import Data.Char
import Data.List

--zad1.begin
digits :: String -> String
digits x = [n| n <-x, isDigit n]

normalize :: String -> String
normalize msg
 |digits msg == [] = [toUpper x| x<-msg, isLetter x]
 |otherwise = error "digits not allowed"
 
 

--zad2a.begin
encode :: [Char] -> Char -> Int -> Char
encode alphabet ch offset
 |contains alphabet ch = encoder alphabet ch offset
 |otherwise = error ("unsupported symbol: " ++ ch:[])
 where
 contains :: String -> Char -> Bool
 contains alphabet ch = [] /= [x| x<-alphabet, x == ch]
 encoder :: [Char] -> Char -> Int -> Char
 encoder alphabet ch offset
  |new_offset == 0 = ch
  |new_offset > 0 = (alphabet ++ alphabet) !! (new_offset + offset_from_beg)
  |otherwise = (alphabet ++ alphabet) !! (new_offset + offset_from_beg + (length alphabet))
  where
  new_offset = (mod offset (length alphabet))
  offset_from_beg = findIndex ch alphabet 0
   where
   findIndex :: Char -> [Char] -> Int -> Int
   findIndex ch alphabet x
    |ch == (head alphabet) = x
    |otherwise = findIndex ch (tail alphabet) (x + 1)

--zad2b.begin
encrypt :: [Char] -> Int -> [Char] -> [Char]
encrypt alphabet offset normalized = map (\ch -> encode alphabet ch offset) normalized

--zad2c.begin
decrypt :: [Char] -> Int -> [Char] -> [Char]
decrypt alphabet offset encrypted = map (\ch -> encode alphabet ch (- offset)) encrypted

--zad3a.begin
crackall :: [Char] -> [Char] -> [String]
crackall alphabet encrypted = decoder alphabet encrypted ((length alphabet) - 1) [] 
 where
 decoder :: [Char] -> [Char] -> Int -> [[Char]] -> [[Char]]
 decoder alphabet encrypted len current
  | len == 0 = current
  | otherwise = decoder alphabet encrypted (len - 1) (current ++ [decrypt alphabet len encrypted]) 
  
--zad3b.begin
substring :: String -> String -> Bool
substring sub str
 |(length sub) > (length str) = False
 |sub == [] = True
 |str == [] = False
 |otherwise = helper sub str sub str
  where
  helper :: String -> String -> String -> String -> Bool
  helper sub str cursub curstr
   |cursub == [] = True
   |curstr == [] = False
   |otherwise = if (head cursub) == (head curstr)
                then helper sub str (tail cursub) (tail curstr)
                else helper sub (tail str) sub (tail str) 
                
--zad3c.begin
crackcandidates :: String -> [String] -> String -> [String]
crackcandidates alphabet commonwords encrypted
 | commonwords == [] = crackall alphabet encrypted
 | alphabet == [] = []
 | otherwise = helper commonwords (crackall alphabet encrypted)
  where
  helper :: [String] -> [String] -> [String]
  helper commonwords decrypted = [st| st <- decrypted, (foldr1 (||) (map (\strin -> substring strin st) commonwords))]

--zad4a.begin
polyencrypt :: String -> Int -> Int ->Int -> String -> String
polyencrypt alphabet offset step blockSize normalized
 |normalized == [] = []
 |otherwise = (encrypt alphabet offset (take blockSize normalized)) ++ (polyencrypt alphabet (offset + step) step blockSize (drop blockSize normalized))
  
--zad4a.begin
polydecrypt :: String -> Int -> Int ->Int -> String -> String
polydecrypt alphabet offset step blockSize encrypted = polyencrypt alphabet (- offset) (- step) blockSize encrypted

--zad5a.begin
enigmaencrypt :: String -> [(Int,Int,Int)] -> String -> String
enigmaencrypt alphabet rotors normalized
 |rotors == [] = normalized
 |otherwise = (enigmaencrypt alphabet (tail rotors) (polyencrypt alphabet offset step blockSize normalized))
 where
 offset    = head [a | (a,b,c) <- [(head rotors)]]
 step      = head [b | (a,b,c) <- [(head rotors)]]
 blockSize = head [c | (a,b,c) <- [(head rotors)]]
 
 --zad5b.begin
enigmadecrypt :: String -> [(Int,Int,Int)] -> String -> String
enigmadecrypt alphabet rotors normalized = helper alphabet (reverse rotors) normalized
 where
 helper :: String -> [(Int,Int,Int)] -> String -> String
 helper alphabet rotors normalized
  |rotors == [] = normalized
  |otherwise = (helper alphabet (tail rotors) (polydecrypt alphabet offset step blockSize normalized))
  where
  offset    = head [a | (a,b,c) <- [(head rotors)]]
  step      = head [b | (a,b,c) <- [(head rotors)]]
  blockSize = head [c | (a,b,c) <- [(head rotors)]]
