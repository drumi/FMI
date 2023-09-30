#lang racket

(define (arcsin z)
    (define (myround x) ;Закръгля до 6-тата цифра след десетичната запетая
        (/ (round (* x 1e6)) 1e6))
    (define (helpersin z oldz newz n memory zsquared nsquared) ;Във memory се пази новото събираемо
        (define (update-memory zsquared nsquared memory) ;update-memory изпозва, че следващото z се получава чрез предходното като се умножи по (z*z*n*n)/((n+1)*(n+2))
            (/ (* memory zsquared nsquared) (+ nsquared n n n 2)));(*(+ n 1) (+ n 2))
        (if (< (- newz oldz) 1.4e-12); сравнение на Δz до 1.4е-12, точността се губи при много близки стойности до 1 или -1
            newz
            (helpersin z newz (+ newz memory) (+ n 2) (update-memory zsquared nsquared memory) zsquared (+ nsquared n n n n 4))))
     (cond [(= z 0) 0] ; При z = 0 уравнението е тривиално
           [(< z 0) (- (myround (helpersin (- z) (- z) (+ (- z) (* 0.5 1/3 (- z) z z)) 5 (* 0.5 0.75 0.2 (- z) z z z z)(* z z) 25)))]
           [else    (myround (helpersin z z (+ z (* 0.5 1/3 z z z)) 5 (* 0.5 0.75 0.2 z z z z z)(* z z) 25))]))

(define (sum-of-greater-primes n k)
    (define (helper n k i sum) ; k е просто в helper
        (if (= n i)
            sum
            (helper n (bigger-prime k) (+ i 1) (+ sum k))))
    (define (prime? x) ;Проверява дали x е просто
        (define (helper? x n)
               (if (> (* n n) x) ;Проверяваме до корен от x
                   #t
                   (if (= 0 (remainder x n))
                        #f
                       (helper? x (+ n 2)))))
        (cond [(= x 1) #t]
              [(= x 2) #t]
              [(= 0 (remainder x 2)) #f] ;Изключваме четните числа
              [else      (helper? x 3)]))
    (define (bigger-prime x) ;Връща следващото просто число след x
        (define (helper y)
            (if (prime? y)
                 y
                 (helper (+ y 2))))
        (cond [(= x 1) 2]
              [(odd? x) (helper (+ x 2))]
              [else     (helper (+ x 1))]))
    (helper n (bigger-prime k) 0 0))

(define (automorphic? n)
    (define(get-number x) ;Връща броя на цифрите на n
       (define (helper x y z)
               (if (< (- x y) 0)
                   z
                   (helper x (* 10 y) (+ z 1))))
       (helper x 10 1))
    (if (or (= (remainder n 10) 0) (= (remainder n 10) 1) (= (remainder n 10) 5) (= (remainder n 10) 6))
        (= 0 (remainder (- (* n n) n) (expt 10 (get-number n))))
        #f)) ;Ако цифрата на единиците на n е различна от 0 , 1 , 5 , 6 то n не е automorphic


        
