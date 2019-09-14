package ru.otus.classes.memento;

import ru.otus.classes.ATMImpl;

import java.util.Map;
import java.util.Set;

public class Memento implements ru.otus.interfaces.memento.Memento {

    //у меня два файла состояния, а не один, как у Лапина. Не помню, зачем мне нужно было atms сохранять отдельно от кассет, я кажется, хотел расширить задачу, чтобы atms не удалялись совсем из системы, а хранились как объекты, чтобы их можно было не создать заново, а восстановить из хранилища. Типа atms те же самые, а не новые, просто их то увозят на ремонт, то возвращают в департамент. Но потом это не понадобилось, но два состояния я оставил как пример того, что их не обязательно делать одним полем класса, поэтому я решил их оставить для примера. Просто интересно стало сделать мементо с двумя полями состояния, как это будет выглядеть

    private Set<ATMImpl> atmsState; //сохраненное состояние atm
    private Map<ATMImpl, Map<Long, Integer>> atmCassettesState; //сохраненное состояние всех кассет в каждом atm

    public Memento(Set<ATMImpl> atmsState, Map<ATMImpl, Map<Long, Integer>> atmCassettesState) {
        this.atmsState = atmsState;
        this.atmCassettesState = atmCassettesState;
    }

    @Override
    public Set<ATMImpl> getAtmsState() {
        return atmsState;
    }
    @Override
    public Map<ATMImpl, Map<Long, Integer>> getAtmCassettesState() {
        return atmCassettesState;
    }

    public void setAtmsState(Set<ATMImpl> atmsState) {
        this.atmsState = atmsState;
    }

    public void setAtmCassettesState(Map<ATMImpl, Map<Long, Integer>> atmCassettesState) {
        this.atmCassettesState = atmCassettesState;
    }
}
