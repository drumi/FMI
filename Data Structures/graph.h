
#ifndef GRAPH_H
#define GRAPH_H

#include <list>

template<class T>
class graph
{
    public:
        graph<T>()
        {

        }

        ~graph<T>()
        {

        }

        graph<T>(graph<T> const& gr)
        {
            g = gr;
        }

        graph<T>& operator=(graph<T> const& gr)
        {
            if(this != &gr)
                g = gr;
            return *this;
        }

        void addTop(const T& a)
        {
            std::list<T> l;
            l.push_back(a);
            g.push_back(l);
        }

        void addRib(T const& x,T const& y)
        {
            typename std::list<std::list<T>>::iterator it = g.begin();
            while(*((*it).begin()) != x)
                it++;
            (*it).push_back(y);
        }

        void deleteRib(T const& x,T const& y)
        {
            typename std::list<std::list<T>>::iterator it1 = g.begin();
            while((*((*it1).begin()) != x) && it1 != g.end())
                it1++;
            if(it1 == g.end())
                return;

            typename std::list<T>::iterator it2 = (*it1).begin();
            while((*it2 != y)&& it2 != (*it1).end())
                it2++;
            if(it2 == (*it1).end())
                return;
            (*it1).erase(it2);
        }

        void deleteTop(T const& x)
        {
            typename std::list<std::list<T>>::iterator it;
            typename std::list<std::list<T>>::iterator it2;
            for(it = g.begin(); it != g.end(); it++)
            {
                if(*(*it).begin() != x)
                    (*it).remove(x);
                else
                    it2 = it;
            }
            g.erase(it2);
        }

        bool top(const T& x)
        {
            typename std::list<std::list<T>>::iterator it = g.begin();
            while(it != g.end() && (*(*it)) != x)
                it++;
            if(it == g.end())
                return false;
            return true;
        }

        bool rib(const T& x, const T& y)
        {
            typename std::list<std::list<T>>::iterator it1 = g.begin();
            while(it1 != g.end() && (*(*it1)) != x)
                it1++;
            if(it1 == g.end())
                return false;

            typename std::list<T>::iterator it2 = (*it1).begin();
            while(it2 != (*it1).end() && *it2 != y)
                it2++;
            if(*it2 == y)
                return true;
            return false;
        }

        bool empty(){ return g.size() == 0; }

        std::list<T> vertexes()
        {
            std::list<T> res;
            typename std::list<std::list<T>>::iterator it = g.begin();
            while(it != g.end())
                res.push_back(*(*it));
            return res;
        }

        void print()
        {
            typename std::list<std::list<T>>::iterator it1;
            typename std::list<std::list<T>>::iterator it3;
            typename std::list<T>::iterator it2;

            for(it1 = g.begin(); it1 != g.end(); it1++)
            {
                for(it2 = (*it1).begin(); it2 != (*it1).end(); it2++)
                {
                    std::cout << *it2;
                    if(it2 == (*it1).begin())
                        std::cout <<" >- ";
                    std::cout << " ";
                }
                std::cout << "|\n";
            }
        }

        void create()
        {
            char ch = 'y';
            std::cout <<"Top y/n? ";
            std::cin >> ch;
            while(ch == 'y')
            {
                std::cout <<"Add top: ";
                T x; std::cin >> x;
                std::cout <<"Top y/n? ";
                std::cin >> ch;
                addTop(x);
            }

            std::cout <<"Rib y/n? ";
            std::cin>> ch;
            while(ch == 'y')
            {
                std::cout <<"Add rib: ";
                T y, z; std::cin >> y >> z;
                addRib(y, z);
                std::cout <<"Rib y/n? ";
                std::cin>> ch;
            }
        }
    private:
    std::list<std::list<T>> g;
};

#endif // GRAPH_H
