
#ifndef AVL_H
#define AVL_H

#include <iomanip>

template<class T>
struct node
{
    node()
    :left(nullptr),right(nullptr),balance(0)
    {}
    T inf;
    node<T>* left;
    node<T>* right;
    int balance;
};

template<class T>
class AVL
{
    public:
        AVL()
        :root(nullptr){ }
        ~AVL(){ deleteAVL(root); }
        AVL(const AVL& avl){ copy(root, avl.root); }
        AVL& operator=(AVL const& avl)
        {
            if(this != &avl)
            {
                deleteAVL(root);
                copy(root,avl.root);
            }
            return *this;
        }

        bool empty() const { return root == nullptr; }
        T rootAVL() const { return root->inf; }
        node<T>* getRoot() const { return root; }

        int balanceAVL() const { return root->balance; }

        AVL<T> leftAVL() const
        {
            AVL<T> t;
            copy(t.root, root->left);
            return t;
        }

        AVL<T> rightAVL() const
        {
            AVL<T> t;
            copy(t.root, root->right);
            return t;
        }

        int insertElem(node<T>*& r, const T& x)
        {
            int h = 0;
            if(!r)
            {
                r = new node<T>;
                r->inf = x;
                r->balance = 0;
                r->left = r->right = nullptr;
                h = 1;
            }else if(x > r->inf)
            {
                if(insertElem(r->right, x))
                {
                    r->balance++;
                    if(r->balance == 1)
                        h = 1;
                    else if(r->balance == 2)
                    {
                        if(r->right->balance == -1)
                            rightRotate(r->right);
                        leftRotate(r);
                    }
                }
            }else if(x < r->inf)
            {
                if(insertElem(r->left, x))
                {
                    r->balance--;
                    if(r->balance == -1)
                        h = 1;
                    else if(r->balance == -2)
                    {
                        if(r->left->balance == 1)
                            leftRotate(r->left);
                        rightRotate(r);
                    }

                }
            }
            return h;
        }

        void createAVL()
        {
            char ch = 'y';
            while(ch == 'y')
            {
                T x;
                std::cout << ">"; std::cin >> x;
                insElem(x);
                std::cout << "y/n? "; std::cin >> ch;
            }
        }

        void insElem(T const& x){ insertElem(root, x); }

        void deleteElem(const T& x){ delElem(root, x); }

        void print() const { pr(root, 0); }

    private:
        node<T>* root;

        int delElem(node<T>*& r, const T& x)
        {
            node<T>* p, *temp;
            int h = 0;
            if(!r)
                return 0;
            if(x < r->inf)
            {
                if(delElem(r->left,x))
                {
                    r->balance++;
                    if(r->balance == 0)
                        h = 1;
                    else if(r->balance == 2)
                    {
                        if(r->right->balance == -1)
                            rightRotate(r->right);
                        leftRotate(r);
                        h = 1;
                    }
                }
            }else if(x > r->inf)
            {
                if(delElem(r->right, x))
                {
                    r->balance--;
                    if(r->balance == 0)
                        h = 1;
                    else if(r->balance == -2)
                    {
                        if(r->left->balance == 1)
                            leftRotate(r->left);
                        rightRotate(r);
                        h = 1;
                    }
                }
            }else
            {
                if(!r->right)
                {
                    temp = r;
                    r = r->left;
                    delete temp;
                    return 1;
                }else if(!r->left)
                {
                    temp = r;
                    r = r->right;
                    delete temp;
                    return 1;
                }else
                {
                    p = r->left;
                    while(p->right)
                        p = p->right;
                    r->inf = p->inf;
                    p->inf = x;
                    if(delElem(r->left, x))
                    {
                        r->balance++;
                        if(r->balance == 0)
                            h = 1;
                        else if(r->balance == 2)
                        {
                            if(r->right->balance == -1)
                                rightRotate(r->right);
                            leftRotate(r);
                            if(r->balance == 0)
                                h = 1;
                        }
                    }
                }
            }
            return h;
        }

        void pr(node<T>* p, int n) const
        {
            if(p)
            {
                n+= 7;
                pr(p->right, n);
                std::cout << std::setw(n) << p->inf << " " << p->balance << std::endl;
                pr(p->left, n);
            }
        }

        void deleteAVL(node<T>*& n) const
        {
            if(n)
            {
                deleteAVL(n->left);
                deleteAVL(n->right);
                delete n;
                n = nullptr;
            }
        }

        void copy(node<T>*& des,node<T>* const& src)
        {
            des = nullptr;
            if(src)
            {
                des = new node<T>;
                des->inf = src->inf;
                des->balance = src->balance;
                copy(des->left, src->left);
                copy(des->right, src->right);
            }
        }

        void rightRotate(node<T>*& r)
        {
            node<T>* p = r;
            r = r->left;
            p->left = r->right;
            r->right = p;

            p->balance = p->balance + 1;
            if(r->balance < 0)
                p->balance = p->balance - r->balance;
            r->balance = r->balance + 1;
            if(p->balance > 0)
                r->balance = p->balance + r->balance;
        }

        void leftRotate(node<T>*& r)
        {
            node<T>* p = r;
            r = r->right;
            p->right = r->left;
            r->left = p;

            p->balance--;
            if(r->balance > 0)
                p->balance -= r->balance;
            r->balance--;
            if(p->balance < 0)
                r->balance += p->balance;

        }
};

#endif // AVL_H
