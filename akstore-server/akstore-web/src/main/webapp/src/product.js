

var TimeFilterItem = React.createClass({
    render: function() {
        var className=this.props.selected?"selected":"n";
        return (<th className={className} onClick={this.props.onClick}><a href="#"><div>{this.dateString()}</div><div>{this.props.item.label}</div></a></th>);
    },
    dateString:function(){
        return new Date(this.props.item.from).Format("MM-dd");
    }
});


var ProductRow = React.createClass({
    handleClick:function(){
        alert('a');
    },
    render: function() {
        var name = this.props.product.stocked ?
            this.props.product.name :
            <span style={{color: 'red'}}>
                {this.props.product.name}
            </span>;
        return (
            <table onClick={this.handleClick} width="100%" className="ProductRow">
                <thead>
                    <tr>
                        <th colSpan="4">{name}</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td width="60"><div className="avatar"><img src="http://segmentfault.com/img/bVbYA8"/></div></td>
                        <td colSpan="3">
                            <div><div><a href="#" ><b>[羽毛球]5月9日 晚上9:01</b></a></div></div>
                            <div>天际运动馆</div>
                        </td>
                    </tr>
                    <tr>
                        <td><div></div></td>
                        <td><div>0/21人</div></td>
                        <td><div>19:00</div></td>
                        <td><div>{this.props.product.price}</div></td>
                    </tr>
                    <tr>
                        <td><div><b>报名中</b></div></td>
                        <td colSpan="3">
                            <div></div>
                        </td>
                    </tr>
                </tbody>
            </table>
        );
    }
});

var ProductList = React.createClass({
    getInitialState: function() {
        return {products: []};
    },
    componentDidMount: function() {
        console.log('componentDidMount');
        this.setState({products: [
                                   {category: 'Sporting Goods', price: '$49.99', stocked: true, name: 'Football'},
                                   {category: 'Sporting Goods', price: '$9.99', stocked: true, name: 'Baseball'},
                                   {category: 'Sporting Goods', price: '$29.99', stocked: false, name: 'Basketball'},
                                   {category: 'Electronics', price: '$99.99', stocked: true, name: 'iPod Touch'},
                                   {category: 'Electronics', price: '$399.99', stocked: false, name: 'iPhone 5'},
                                   {category: 'Electronics', price: '$199.99', stocked: true, name: 'Nexus 7'}
                                 ]});
    },
    render: function() {
        console.log(this.props.url);
        var rows = [];
        var lastCategory = null;
        this.state.products.map(function(product) {
            rows.push(<ProductRow product={product} key={product.name} />);
            lastCategory = product.category;
        });
        return (

                <div className="ProductList">{rows}</div>
        );
    }
});

var FilterableProductTable = React.createClass({
    initFilterItems:function(selectedItem){
        var nowTimeStamp=new Date().getTime();
        nowTimeStamp=nowTimeStamp-nowTimeStamp%(24*3600000)-8*3600000;
        var items= [
                                                   {label:"今天",selected:false,from:nowTimeStamp,to:nowTimeStamp+3600000*24},
                                                   {label:"明天",selected:false,from:nowTimeStamp+3600000*24,to:nowTimeStamp+3600000*24*2},
                                                   {label:"后天",selected:false,from:nowTimeStamp+3600000*24*2,to:nowTimeStamp+3600000*24*3},
                                                   {label:"两天后",selected:false,from:nowTimeStamp+3600000*24*3,to:nowTimeStamp+3600000*24*5}
                                               ];
        if(selectedItem)
            items[selectedItem-1].selected=true;
        return items;
    },
    getInitialState: function() {
            return {url:null,items:this.initFilterItems(1)};
    },
    handleClick: function(item,i) {
        //console.log('You clicked: ' + item);
        var state={url:i};
        state.items=this.initFilterItems(i+1);
        this.setState(state);
    },
    render: function() {
        var items=[];
        var self=this;
        this.state.items.map(function(item,i){
            items.push(<TimeFilterItem item={item} selected={item.selected} onClick={self.handleClick.bind(self,item,i)} />);
        });
        return (
            <div>
                <table width="100%" className="TimeFilterTable">
                   <thead><tr>{items}</tr></thead>
                </table>
                <ProductList url={this.state.url} />
            </div>
        );
    }
});