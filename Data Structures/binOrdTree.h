#ifndef BALANCEDBINORDTREE_H
#define BALANCEDBINORDTREE_H

template<class T>
struct node
{
    node()
    :left(nullptr), right(nullptr)
    {}

    T inf;
    node *left, *right;
};

template<class T>
class balancedBinOrdTree
{
    public:
        balancedBinOrdTree()
        :root(nullptr)
        {

        }
        balancedBinOrdTree(int n)
        :root(nullptr)
        {
            if(n>0)
            {
                int nLeft = (n-1)/2;
                int nRight = n - nLeft - 1;
                balancedBinOrdTree t1(nLeft);
                T x; std::cin>> x;
                balancedBinOrdTree t2(nRight);
                create3(x,t1,t2);
            }
        }
        ~balancedBinOrdTree()
        {
            deleteTree(root);
        }
        balancedBinOrdTree(balancedBinOrdTree const& tree)
        {
            copyTree(tree);
        }
        balancedBinOrdTree& operator=(balancedBinOrdTree const& tree)
        {
            if(this != &tree)
            {
                deleteTree(root);
                copyTree(tree);
            }
            return *this;
        }

        node<T>* getRoot() const
        {
            return root;
        }

        T rootTree() const
        {
            return root->inf;
        }

        bool empty()
        {
            return root == nullptr;
        }

        balancedBinOrdTree leftTree() const
        {
            balancedBinOrdTree<T> tree;
            copy(tree->left, root->left);
        }

        balancedBinOrdTree rightTree() const
        {
            balancedBinOrdTree<T> tree;
            copy(tree->right, root->right);
        }

        void create3(T const& x, balancedBinOrdTree<T> const& l, balancedBinOrdTree<T> const& r)
        {
            deleteTree(root);
            root = new node<T>;
            root->inf = x;
            copy(root->left, l.root);
            copy(root->right, r.root);
        }

        void create()
        {
            root = nullptr;
            T x; char c;
            do
            {
                std::cout << ">"; std::cin >> x;
                addNode(x);
                std::cout<<"next elem y/n? "; std::cin >> c;
            } while(c == 'y');
        }

        void print()
        {
            pr(root);
            std::cout << std::endl;
        }

        void addNode(T const& x)
        {
            add(root, x);
        }

        void deleteNode(const T& x)
        {
            del(root, x);
        }

        T findMin() const
        {
            T min;
            node<T>* n = root;
            while(n)
            {
                min = n->inf;
                n = n->left;
            }
            return min;
        }

        T findMax() const
        {
            T max;
            node<T>* n = root;
            while(n)
            {
                max = n->inf;
                n = n->right;
            }
            return max;
        }
    private:
        node<T>* root;

        void del(node<T>* rt, T const& x)
        {
            node<T>* nullifyLeft = nullptr;
            node<T>* nullifyRight = nullptr;

            while(!(rt->inf == x))
            {
                if(!rt)
                {
                    std::cerr << "Tree doesn't have member: " <<x<<'\n';
                    return;
                }

                if(x < rt -> inf)
                {
                    nullifyLeft = rt;
                    nullifyRight = nullptr;
                    rt = rt -> left;
                }
                else if(x > rt->inf)
                {
                    nullifyRight = rt;
                    nullifyLeft = nullptr;
                    rt = rt->right;
                }
            }

            node<T>* pos = rt;

            if(rt -> left)
            {
                nullifyLeft = rt;
                rt = rt -> left;
                while(rt -> right)
                {
                    nullifyLeft = nullptr;
                    nullifyRight = rt;
                    rt = rt -> right;
                }
            } else if(rt -> right)
            {
                nullifyRight = rt;
                rt = rt -> right;
                while(rt -> left)
                {
                    nullifyRight = nullptr;
                    nullifyLeft = rt;
                    rt = rt -> left;
                }
            }
            pos -> inf = rt -> inf;

            if(nullifyLeft)
                nullifyLeft->left = nullptr;
            if(nullifyRight)
                nullifyRight->right = nullptr;
            if(rt == root)
                root = nullptr;
            delete rt;
        }

        void add(node<T>*& p, T const& x)
        {
            if(!p)
            {
                p = new node<T>;
                p->inf = x;
            }
            else
                if(x < p->inf)
                    add(p->left, x);
            else
                add(p->right, x);
        }

        void pr(node<T>* rt)
        {
            if(rt)
            {
                pr(rt->left);
                std::cout << rt->inf << " ";
                pr(rt->right);
            }
        }
        void deleteTree(node<T>* rt)
        {
            if(rt)
            {
                deleteTree(rt->left);
                deleteTree(rt->right);
                delete rt;
            }
        }

        void copy(node<T>*& des, node<T>* src) const
        {
            des = nullptr;
            if(src)
            {
                des = new node<T>;
                des->inf = src->inf;
                copy(des->left, src->left);
                copy(des->right, src->right);
            }
        }

        void copyTree(balancedBinOrdTree const& tree)
        {
            copy(root, tree.root);
        }

};

#endif // BALANCEDBINORDTREE_H
