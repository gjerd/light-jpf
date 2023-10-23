module light.jpf {
    requires commons.lang3;
    requires org.slf4j;
    requires lombok;

    exports ljpf;
    exports ljpf.loader;
    exports ljpf.repository;
    exports ljpf.versions;

}