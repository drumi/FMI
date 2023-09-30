#ifndef BINTREE_H
#define BINTREE_H


template<class T>
struct node
{
    node()
    : left(nullptr), right(nullptr)
    {

    }
    T inf;
    node *left,
         *right;
};

template<class T>
class binTree
{
    public:
        binTree();
        binTree(T& x);
        ~binTree();
        binTree(binTree<T> const&);
        binTree& operator=(binTree<T> const&);

        bool empty() const;
        void print() const;

        node<T>* getRoot() const;
        T rootTree() const;

        binTree leftTree() const;
        binTree rightTree() const;

        void create(){createTree(root);}
        void create3(const T&, const binTree<T>&, const binTree<T>&);
    private:
        node<T>* root;
        void pr(node<T>*) const;
        void deleteTree(node<T>*&) const;
        void copy(node<T>*&, node<T>* const&) const;
        void copyTree(const binTree<T>&);
        void createTree(node<T>*&);
};

#endif // BINTREE_H

template<class T>
binTree<T>::binTree()
:root(nullptr)
{

}

template<class T>
binTree<T>::binTree(T& x)
:root(nullptr)
{
    root = new node<T>;
    root->inf = x;
}

template<class T>
binTree<T>::~binTree()
{
    deleteTree(root);
}

template<class T>
binTree<T>::binTree(binTree<T> const& t)
{
    copyTree(t);
}

template<class T>
binTree<T>& binTree<T>::operator=(binTree<T> const& t)
{
    if(this != &t)
    {
        deleteTree(root);
        copyTree(t);
    }
    return *this;
}

template<class T>
void binTree<T>::deleteTree(node<T>*& r) const
{
    if(r)
    {
        deleteTree(r->left);
        deleteTree(r->right);
        delete r;
        r = nullptr;
    }
}

template<class T>
void binTree<T>::copy(node<T>*& pos, node<T>* const& r) const
{
    pos = nullptr;
    if(r)
    {
        pos = new node<T>;
        pos -> inf = r ->inf;
        copy(pos->left, r->left);
        copy(pos->right, pos->right);
    }
}

template<class T>
void binTree<T>::copyTree(const binTree<T>& r)
{
    copy(root, r.root);
}

template<class T>
bool binTree<T>::empty() const
{
    return root == nullptr;
}

template<class T>
node<T>* binTree<T>::getRoot() const
{
    return root;
}

template<class T>
T binTree<T>::rootTree() const
{
    return root->inf;
}

template<class T>
binTree<T> binTree<T>::leftTree() const
{
    binTree<T> tree;
    copy(tree.root, root->left);
    return tree;
}

template<class T>
binTree<T> binTree<T>::rightTree() const
{
    binTree<T> tree;
    copy(tree.root, root->right);
    return tree;
}

template<class T>
void binTree<T>::create3(T const& x, const binTree<T>& l, const binTree& r)
{
    if(root)
        deleteTree(root);
    root = new node<T>;
    root ->inf = x;
    copy(root->left, l.root);
    copy(root->right, r.right);

}

template<class T>
void binTree<T>::print() const
{
    pr(root);
    std::cout << std::endl;
}

template<class T>
void binTree<T>::pr(node<T>* r) const
{
    if(r)
    {
        pr(r->left);
        std::cout << r->inf << " ";
        pr(r->right);
    }
}

template<class T>
void binTree<T>::createTree(node<T>*& pos)
{
    T x;
    char c;
    std::cout << "root: ";
    std::cin >> x;
    pos = new node<T>;
    pos->inf = x;
    pos->left = nullptr;
    pos->right = nullptr;
    std::cout << "Left Tree of: " << x << " y/n? ";
    std::cin >> c;
    if (c == 'y') createTree(pos->left);
    std::cout << "Right Tree of: " << x << " y/n? ";
    std::cin >> c;
    if (c == 'y') createTree(pos->right);
}

