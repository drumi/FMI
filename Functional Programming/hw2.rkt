#lang racket

(define (cartesian-product xs ys)
   (define (helper cls rxs rys)
       (if (null? rxs)
            cls
            (if (null? rys)
                (helper cls (cdr rxs) ys)
                (helper (append cls (cons (cons (car rxs) (car rys)) '())) rxs (cdr rys)))))
  (helper '() xs ys))


(define (latin-square? xss)
   (define (has-from-1-n? ls n)
      (cond [(= n 0) #t]
            [(member n ls) (has-from-1-n? ls (- n 1))]
            [else #f]))
   (define (latin-square-by-rows? ls n)
      (if (null? ls)
           #t
           (and (has-from-1-n? (car ls) n) (latin-square-by-rows? (cdr ls) n))))
   (define (get-nth-remaining-list ls n)
     (if (= n 0)
         (car ls)
         (get-nth-remaining-list (cdr ls) (- n 1))))
  (define (rotate-matrix xs)
     (define (helper curxs l n)
        (if (= l n)
             curxs
            (helper (append curxs (cons (map (lambda (ls) (get-nth-remaining-list ls l)) xs) '())) (+ l 1) n)))
     (helper '() 0 (length xs)))
  (and (latin-square-by-rows? xss (length xss)) (latin-square-by-rows? (rotate-matrix xss) (length xss))))



(define (pair-compose fs)
   (define (composed-list fs)
      (define (helper rls list-of-composed)
         (if (null? rls)
              list-of-composed
              (helper (cddr rls) (append list-of-composed (list (lambda (x) ((eval (car rls)) ((eval (cadr rls)) x))))))))
      (if (even? (length fs))
         (helper fs '())
         (helper (append fs (list (lambda (x) x))) '())))
   (define (composed-sum ls x)
      (if (null? ls)
           0
           (+ ((eval (car ls)) x) (composed-sum (cdr ls) x))))
  (lambda (x) (composed-sum (composed-list fs) x)))