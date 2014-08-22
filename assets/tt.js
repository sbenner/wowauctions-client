//modified http://static-azeroth.cursecdn.com/current/js/syndication/tt.js to load local css
(function () {

    window.HTMLDiff = (function () {

        function HTMLDiff(a, b) {
            this.a = a;
            this.b = b;
        }

        HTMLDiff.prototype.diff = function () {
            var diff;
            diff = this.diff_list(this.tokenize(this.a), this.tokenize(this.b));
            this.update(this.a, diff.filter(function (_arg) {
                var status, text;
                status = _arg[0], text = _arg[1];
                return status !== '+';
            }));
            return this.update(this.b, diff.filter(function (_arg) {
                var status, text;
                status = _arg[0], text = _arg[1];
                return status !== '-';
            }));
        };

        HTMLDiff.prototype.parseTextNodes = function (node, callback) {
            var handleNode;
            handleNode = function (node) {
                if (node == null) { return false; }
                var n, new_node, new_nodes, old_node, _i, _j, _len, _len2, _ref;
                if (node.nodeType === 3) {
                    if (!/^\s*$/.test(node.nodeValue)) return callback(node);
                } else {
                    _ref = (function () {
                        var _j, _len, _ref, _results;
                        _ref = node.childNodes;
                        _results = [];
                        for (_j = 0, _len = _ref.length; _j < _len; _j++) {
                            n = _ref[_j];
                            _results.push(n);
                        }
                        return _results;
                    })();
                    for (_i = 0, _len = _ref.length; _i < _len; _i++) {
                        old_node = _ref[_i];
                        new_nodes = handleNode(old_node);
                        if (new_nodes) {
                            for (_j = 0, _len2 = new_nodes.length; _j < _len2; _j++) {
                                new_node = new_nodes[_j];
                                node.insertBefore(new_node, old_node);
                            }
                            node.removeChild(old_node);
                        }
                    }
                    return false;
                }
            };
            return handleNode(node);
        };

        HTMLDiff.prototype.tokenize = function (root) {
            var tokens;
            tokens = [];
            this.parseTextNodes(root, function (node) {
                tokens = tokens.concat(node.nodeValue.split(' '));
                return false;
            });
            return tokens;
        };

        HTMLDiff.prototype.update = function (root, diff) {
            var pos;
            pos = 0;
            return this.parseTextNodes(root, function (node) {
                var end, ins_node, new_node, new_nodes, output, part, start, status, text, _i, _len, _ref;
                start = pos;
                end = pos + (node.nodeValue.split(' ')).length;
                pos = end;
                output = (function () {
                    var _i, _len, _ref, _ref2, _results;
                    _ref = diff.slice(start, end);
                    _results = [];
                    for (_i = 0, _len = _ref.length; _i < _len; _i++) {
                        _ref2 = _ref[_i], status = _ref2[0], text = _ref2[1];
                        if (status === '=') {
                            _results.push(text);
                        } else {
                            _results.push('<ins>' + text + '</ins>');
                        }
                    }
                    return _results;
                })();
                output = output.join(' ').replace(/<\/ins> <ins>/g, ' ').replace(/<ins> /g, ' <ins>').replace(/[ ]<\/ins>/g, '</ins> ').replace(/<ins><\/ins>/g, '');
                new_nodes = [];
                new_node = document.createTextNode("");
                new_nodes.push(new_node);
                _ref = output.split(/(<\/?ins>)/);
                for (_i = 0, _len = _ref.length; _i < _len; _i++) {
                    part = _ref[_i];
                    switch (part) {
                        case '<ins>':
                            ins_node = document.createElement('ins');
                            new_nodes.push(ins_node);
                            new_node = document.createTextNode("");
                            ins_node.appendChild(new_node);
                            break;
                        case '</ins>':
                            new_node = document.createTextNode("");
                            new_nodes.push(new_node);
                            break;
                        default:
                            new_node.nodeValue = part;
                    }
                }
                return new_nodes.filter(function (node) {
                    return !(node.nodeType === 3 && node.nodeValue === '');
                });
            });
        };

        HTMLDiff.prototype.diff_list = function (before, after) {
            var i, j, k, lastRow, ohash, subLength, subStartAfter, subStartBefore, thisRow, val, _i, _len, _len2, _len3, _ref, _ref2;
            ohash = {};
            for (i = 0, _len = before.length; i < _len; i++) {
                val = before[i];
                if (!(val in ohash)) ohash[val] = [];
                ohash[val].push(i);
            }
            lastRow = (function () {
                var _ref, _results;
                _results = [];
                for (i = 0, _ref = before.length; 0 <= _ref ? i < _ref : i > _ref; 0 <= _ref ? i++ : i--) {
                    _results.push(0);
                }
                return _results;
            })();
            subStartBefore = subStartAfter = subLength = 0;
            for (j = 0, _len2 = after.length; j < _len2; j++) {
                val = after[j];
                thisRow = (function () {
                    var _ref, _results;
                    _results = [];
                    for (i = 0, _ref = before.length; 0 <= _ref ? i < _ref : i > _ref; 0 <= _ref ? i++ : i--) {
                        _results.push(0);
                    }
                    return _results;
                })();
                _ref2 = (_ref = ohash[val]) != null ? _ref : [];
                for (_i = 0, _len3 = _ref2.length; _i < _len3; _i++) {
                    k = _ref2[_i];
                    thisRow[k] = (k && lastRow[k - 1] ? 1 : 0) + 1;
                    if (thisRow[k] > subLength) {
                        subLength = thisRow[k];
                        subStartBefore = k - subLength + 1;
                        subStartAfter = j - subLength + 1;
                    }
                }
                lastRow = thisRow;
            }
            if (subLength === 0) {
                return [].concat((function () {
                    var _j, _len4, _results;
                    _results = [];
                    for (_j = 0, _len4 = before.length; _j < _len4; _j++) {
                        val = before[_j];
                        _results.push(['-', val]);
                    }
                    return _results;
                })(), (function () {
                    var _j, _len4, _results;
                    _results = [];
                    for (_j = 0, _len4 = after.length; _j < _len4; _j++) {
                        val = after[_j];
                        _results.push(['+', val]);
                    }
                    return _results;
                })());
            } else {
                return [].concat(this.diff_list(before.slice(0, subStartBefore), after.slice(0, subStartAfter)), (function () {
                    var _j, _len4, _ref3, _results;
                    _ref3 = after.slice(subStartAfter, (subStartAfter + subLength));
                    _results = [];
                    for (_j = 0, _len4 = _ref3.length; _j < _len4; _j++) {
                        val = _ref3[_j];
                        _results.push(['=', val]);
                    }
                    return _results;
                })(), this.diff_list(before.slice(subStartBefore + subLength), after.slice(subStartAfter + subLength)));
            }
        };

        return HTMLDiff;

    })();

}).call(this);

/* c:\CobaltAlt\Source\Curse.Azeroth.Web\..\Curse.Gandalf.Web\Content\js\Gandalf\CurseTip.js */
(function (global) {
    'use strict';

    var list = document.getElementsByTagName('script'),
        path = list[list.length - 1].src;

    var px = function(num) {
        return num + 'px';
    };

    var Position = function (_x, _y) {
        this.x = _x || 0;
        this.y = _y || 0;
    };

    var CurseTip = function() {
        CurseTip.prototype.initialize.apply(this, arguments);
    };

    CurseTip.Ready = false;

    CurseTip.bindEvent = function(element, _event, handler) {
        var _this = this;

        if (_event === 'load') {
            var _handler = handler;

            if (element.addEventListener) {
                _event = 'DOMContentLoaded';

                handler = function() {
                    _handler.call(_this);
                    _this.Ready = true;
                };
            } else if (element.attachEvent) {
                _event = 'onreadystatechange';
                element = document;

                handler = function() {
                    if (document.readyState === 'complete' && !_this.Ready) {
                        _handler.call(_this);
                        _this.Ready = true;
                    }
                };
            } else {
                _event = 'onload';

                handler = function() {
                    _handler.call(_this);
                    _this.Ready = true;
                };
            }
        } else if (!element.addEventListener && !element.attachEvent) {
            _event = 'on' + _event;
        }

        if (element.addEventListener) {
            element.addEventListener(_event, function(event) { handler.call(_this, event); });
        } else if (element.attachEvent) {
            element.attachEvent(_event, function(event) { handler.call(_this, event); });
        } else {
            element[_event] = function(event) { handler.call(_this, event); };
        }

        return this;
    };

    CurseTip.unbindEvent = function(element, _event, handler) {
        var _this = this,
            eventHandler;

        if (_event === 'load') {
            var _handler = handler;

            if (element.removeEventListener) {
                _event = 'DOMContentLoaded';

                handler = function() {
                    _handler.call(_this);
                    _this.Ready = true;
                };
            } else if (element.detachEvent) {
                _event = 'onreadystatechange';
                element = document;

                handler = function() {
                    if (document.readyState === 'complete' && !_this.Ready) {
                        _handler.call(_this);
                        _this.Ready = true;
                    }
                };
            } else {
                _event = 'onload';

                handler = function() {
                    _handler.call(_this);
                    _this.Ready = true;
                }
            }
        } else if (!element.removeEventListener && !element.detachEvent) {
            _event = 'on' + _event;
        }

        if (element.removeEventListener) {
            element.removeEventListener(_event, function(event) { handler.call(_this, event); });
        } else if (element.detachEvent) {
            element.detachEvent(_event, function(event) { handler.call(_this, event); });
        } else {
            element[_event] = null;
        }

        return this;
    };

    CurseTip.prototype = {
        Path: path,
        Cache: {},
        Options: {
            AdvancedTooltips: false,
            HashAliases: {},
            LoadingText: 'Loading&hellip;',
            Namespace: 'db-tooltip',
            Offset: new Position(10, 10),
            Paths: [],
            ExtraRegexes: [],
            Url: null
        },
        MousePosition: new Position(0, 0),
        EventHandler: null,
        CurrentElement: null,
        CurrentTitle: null,
        Timeout: null,
        LastPosition: new Position(),
        FirstParty: false,
        RegEx: null,
        MouseOverDocument: false,

        Disabled: false,

        initialize: function () {
            var _this = this;
            // Handle Args
            switch (arguments.length) {
                case 0:
                    return false;
                    break;
                case 1:
                    if (typeof arguments[0] === 'object') {
                        this.setOptions(arguments[0]);
                    } else {
                        this.setOptions({ Url: arguments[0] });
                    }
                    break;
                case 2:
                    this.setOptions({ Url: arguments[0], Namespace: arguments[1] });
                    break;
            }

            var scripts = document.getElementsByTagName('script'),
                script = scripts[scripts.length - 1],
                matches = script.src.match(/(?:.js\?)(.*)$/);

            if (matches && matches.length > 1) {
                var args = matches[1].split(/\&/);

                for (var i in args) {
                    if (args.hasOwnProperty(i)) {
                        var arg = args[i].split(/=/);

                        if (arg[0] = 'var' && arg[1]) {
                            window[arg[1]] = this;
                        }
                    }
                }
            }

            if (this.Options.Url === undefined) {
                return false;
            }

            // Determine Event Handler (If Any)
            if (window.addEventListener) {
                this.EventHandler = 'addEventListener';
            }

            var re = /^(?:.*\/\/)?(?:.*?\.)?((.*)\.(com|net|org|local|dev))$/;

            // Check if this is our site or not (extra checks to do when binding hovers if so)
            try {
                this.FirstParty = (this.Options.Url.match(re)[2] === document.location.host.match(re)[2]);
            } catch (e) { }

            if (!this.FirstParty) {
                var css = document.createElement('link');

                css.type = 'text/css';
                css.rel = 'stylesheet';
                css.href = this.Path.substr(0, this.Path.indexOf('/js/')) + 'tt.css';

                document.getElementsByTagName('head')[0].appendChild(css);
            }

            var paths = this.Options.Paths;
            paths = paths.length > 0 ? paths.join('|') : '';
           // paths = paths.replace(/\//, '\\/');

            // Deconstruct URL for RegExp
            this.RegEx = new RegExp(this.Options.Url.replace(re, '$2.(?:com|net|org|local|dev)/' + (this.Options.Paths.length > 0 ? '(' + this.Options.Paths.join('|') + ')/([\\d]+(?:[\\w-]+)?)(?:\\?(simple|advanced))?(?:#(\\d+)-(\\d+))?' : '')));

            if (!CurseTip.Ready) {
                CurseTip.bindEvent.call(this, window, 'load', this.watchElligibleElements);
            } else {
                this.watchElligibleElements();
            }

            window.CurseTips = window.CurseTips || {};

            while (window.CurseTips[this.Options.Namespace]) {
                this.Options.Namespace += '-' + new Date().getTime();
            }

            window.CurseTips[this.Options.Namespace] = this;
        },

        disable: function () {
            this.Disabled = true;
        },

        enable: function () {
            this.Disabled = false;
        },

        toggle: function () {
            this.Disabled = !this.Disabled;
        },

        setOptions: function (options) {
            var _options = {};

            for (var opt in this.Options) {
                if (this.Options.hasOwnProperty(opt)) {
                    _options[opt] = this.Options[opt];
                }
            }

            for (var opt in options) {
                if (options.hasOwnProperty(opt)) {
                    _options[opt] = options[opt];
                }
            }

            this.Options = _options;
        },

        watchElligibleElements: function () {
            this._watchElements(this.getElligibleElements());
        },

        watchElements: function (elements) {
            if (elements.nodeName && !elements.length) {
                elements = [elements];
            }

            elements = this._processElements(elements);

            if (elements.length) {
                this._watchElements(elements);
            }
        },

        _watchElements: function (elements) {
            for (var i in elements) {
                if (elements.hasOwnProperty(i)) {
                    var element = elements[i];

                    if (!element.nodeName) {
                        continue;
                    }

                    CurseTip.bindEvent.call(this, element, 'mouseover', this.createTooltip);
                    CurseTip.bindEvent.call(this, element, 'mouseout', function (event) {
                        this.handleTooltipData();
                        CurseTip.unbindEvent.call(this, event.currentTarget, 'mousemove', this.moveTooltip);
                    });

                    // Fixes for hanging tooltips
                    CurseTip.bindEvent.call(this, document, 'mouseover', function () {
                        if (!this.MouseOverDocument) {
                            this.MouseOverDocument = true;
                        }

                        return false;
                    });
                    CurseTip.bindEvent.call(this, document, 'mouseout', function () {
                        if (this.MouseOverDocument) {
                            this.handleTooltipData();
                            this.MouseOverDocument = false;
                        }

                        return;
                    });
                }
            }
        },

        _processElements: function (elements) {
            var finalElements = [];

            for (var j in elements) {
                if (elements.hasOwnProperty(j) && !isNaN(j)) {
                    var element = elements[j],
                        href;

                    if (!element.nodeName) {
                        continue;
                    }

                    try {
                        var href = element.getAttribute('data-tooltip-href') || element.href;
                    } catch (e) { }

                    if (!href) {
                        continue;
                    }

                    try {
                        if (element.getAttribute('data-disable-tip') === 'true') {
                            continue;
                        }
                    } catch (e) { }

                    if (href[0] === '/' && href[1] !== '/') {
                        href = '//' + document.location.host + href;
                    }

                    href = href.replace(/\/(#|\?|$)/, '$1').replace(/#$/, '');

                    // Search for hash aliases
                    for (var alias in this.Options.HashAliases) {
                        if (this.Options.HashAliases.hasOwnProperty(alias)) {
                            var _href = href.split('#');

                            if (!_href[1]) {
                                continue;
                            }

                            if (_href[1] === alias) {
                                _href[0] += (_href[0].search(/\?/) >= 0 ? '&' : '?') + this.Options.HashAliases[alias];
                                href = _href[0];
                                element.setAttribute('data-tooltip-href', href);
                            }
                        }
                    }

                    if (this.FirstParty) {
                        if (href.search(new RegExp(document.location.host + document.location.pathname + '$')) > -1) {
                            continue;
                        }
                    }

                    if (href.substr(0, 11) === 'javascript:' || href.length === 0 || href === '#') {
                        continue;
                    }

                    var matches = href.match(this.RegEx);

                    if (matches) {
                        if (href.substr(0, href.search(this.RegEx)).search(/\/\//) === -1) {
                            href = '//' + href;
                        }

                        element.setAttribute('data-tooltip-href', href);

                        if (matches[3]) {
                            element.setAttribute('data-tooltip-mode', matches[3]);
                        }

                        if (matches[4] && matches[5]) {
                            element.setAttribute('data-tooltip-ver1', matches[4]);
                            element.setAttribute('data-tooltip-ver2', matches[5]);
                        }

                        finalElements.push(element);
                    } else {
                        var found = false;
                        for (var reg in this.Options.ExtraRegexes) {
                            if (this.Options.ExtraRegexes.hasOwnProperty(reg)) {
                                var match = href.match(this.Options.ExtraRegexes[reg]);
                                if (!found && match) {
                                    element.setAttribute("data-tooltip-custom", "true");
                                    found = true;
                                    finalElements.push(element);
                                }
                            }
                        }
                    }

                }
            }

            return finalElements;
        },

        getElligibleElements: function () {
            var finalElements = [];

            if (document.querySelectorAll) {
                var elements = document.querySelectorAll('a[href], *[data-tooltip-href]');
            } else {
                // Warning: This is SUPER inefficient, but will only be used on SUPER outdated browsers.
                // document.querySelectorAll() is supported by the browsers/versions we support: http://caniuse.com/#search=queryselector
                var _elements = document.getElementsByTagName('body')[0].getElementsByTagName('*'),
                    elements = [];

                for (var i in _elements) {
                    if (_elements.hasOwnProperty(i)) {
                        var element = _elements[i];

                        if (element.nodeName === 'A' || element.getAttribute('data-tooltip-href')) {
                            elements.push(element);
                        }
                    }
                }
            }

            return this._processElements(elements);
        },

        createTooltip: function (event) {
            if (this.Disabled || !event.currentTarget.getAttribute('data-tooltip-href')) {
                return false;
            }

            var container,
                target = event.currentTarget,
                origHref = target.getAttribute('data-tooltip-href'),
                isCustom = target.getAttribute('data-tooltip-custom'),
                href = origHref,
                parts = null,
                id = null;

            if (!isCustom) {
                var qsa = origHref.split(/\?/)[1];
                href = origHref.split(/\//),
                    parts = href.pop().match(/^(?:(\d+)(?:-[-\w]+?)?)(\?.*?)?(#.*)?$/),
                    id = parts[1];
            }


            var args = [],
                hash = null;

            this.MousePosition.x = event.clientX;
            this.MousePosition.y = event.clientY;

            if (!isCustom) {

                for (var i = 2; i < parts.length; i++) {
                    if (!parts[i]) {
                        continue;
                    }

                    switch (parts[i][0]) {
                        case '?':
                            args = parts[i].substr(1).split(/&/);
                            break;
                        case '#':
                            hash = parts[i];
                            break;
                    }
                }

                href.push(id);
                href = href.join('/');

                if ((this.Options.AdvancedTooltips && target.getAttribute('data-tooltip-mode') !== 'simple') || target.getAttribute('data-tooltip-mode') === 'advanced') {
                    args.push('advanced=1');
                }

                args.push('callback=window.CurseTips[\'' + this.Options.Namespace + '\'].handleTooltipData');
            }

            if (container = document.getElementById('db-tooltip-container')) {
                container.innerHTML = null;
            } else {
                container = document.createElement('div');
                container.id = 'db-tooltip-container';
                document.getElementsByTagName('body')[0].appendChild(container);
            }

            container.className = this.Options.Namespace;

            if (target.getAttribute('data-tooltip-ver1') && target.getAttribute('data-tooltip-ver2')) {
                if (!isCustom) {
                    href += '/dual-tooltip/' + target.getAttribute('data-tooltip-ver1') + '/' + target.getAttribute('data-tooltip-ver2');
                }
                container.className += ' diff';
            } else {
                if (!isCustom) {
                    href += '/tooltip';
                }
            }
            if (!isCustom) {
                href += '?' + args.join('&');
                href = href.replace(/^http(s)?:/, '');
            }

            container.style.position = 'fixed';
            container.style.zIndex = 9999;
            container.style.whiteSpace = 'nowrap';

            var h3 = document.createElement('h3'),
                body = document.createElement('div'),
                tipUrl = document.createElement('div');

            h3.style.display = 'none';

            body.className = 'body';

            tipUrl.className = 'url';

            if (this.Options.ShowURL) {
                tipUrl.innerText = origHref.replace(/^http(s)?:\/\//, '');
            } else {
                tipUrl.style.display = 'none';
            }

            container.appendChild(h3);
            container.appendChild(body);
            container.appendChild(tipUrl);
            container.setAttribute('data-current-tooltip-href', href);

            CurseTip.bindEvent.call(this, target, 'mousemove', this.moveTooltip);

            if (this.Cache[document.location.protocol + href]) {
                this.handleTooltipData(this.Cache[document.location.protocol + href]);
            } else {
                var script = document.createElement('script');
                script.src = href;
                script.setAttribute('data-tooltip-href', origHref);

                document.getElementsByTagName('head')[0].appendChild(script);
                body.innerHTML = this.Options.LoadingText;
                container.style.display = 'block';
            }
        },

        handleTooltipData: function (data) {
            var container = document.getElementById('db-tooltip-container');

            if (!container) {
                return false;
            }

            if (!data) {
                container.style.display = 'none';
                return false;
            }

            var url = container.getAttribute('data-current-tooltip-href').split(/\?/)[0],
                Url = data.Url.split(/\?/)[0];

            if (Url.search(url) < 0) {
                return false;
            }

            this.Cache[data.Url] = data;

            if (container.className === this.Options.Namespace + ' diff') {
                var tmp = document.createElement('div');
                tmp.innerHTML = data.Tooltip;

                var blocks = tmp.getElementsByClassName('db-tooltip');

                if (blocks.length > 2) {
                    var differ = new HTMLDiff(blocks[1], blocks[2]);
                    differ.diff();
                    data.Tooltip = tmp.innerHTML;
                }
            }
            container.getElementsByClassName('body')[0].innerHTML = data.Tooltip;
            container.style.display = 'block';

            var desc = container.getElementsByClassName('db-description');

            for (var i in desc) {
                if (desc.hasOwnProperty(i) && desc[i].style !== undefined) {
                    desc[i].style.whiteSpace = 'normal';
                }
            }

            this.moveTooltip();
        },

        moveTooltip: function (event) {
            if (event) {
                this.MousePosition.x = event.clientX;
                this.MousePosition.y = event.clientY;
            } else {
                event = { clientX: this.MousePosition.x, clientY: this.MousePosition.y };
            }

            if (!this.Options.Offset.x || !this.Options.Offset.y) {
                this.Options.Offset = new Position(10, 10);
            }

            var container = document.getElementById('db-tooltip-container'),
                height = container.offsetHeight,
                width = container.offsetWidth,
                left = event.clientX + this.Options.Offset.x,
                bottom = (window.innerHeight - event.clientY) + this.Options.Offset.y;

            if (event.clientY - height - this.Options.Offset.y < 0) {
                if (event.clientY + height + this.Options.Offset.y <= window.innerHeight) {
                    bottom -= height + (this.Options.Offset.y * 2);
                } else {
                    bottom -= (height / 2) - this.Options.Offset.y;
                }
            }

            if (window.innerWidth - event.clientX - width - this.Options.Offset.x < this.Options.Offset.x) {
                left -= width + (this.Options.Offset.x * 2);
            }

            container.style.left = px(left);
            container.style.bottom = px(bottom);
        }
    };

    CurseTip.bindEvent(window, 'load', function () { this.Ready = true; });

    global.CurseTip = CurseTip;
}(window || this));

/* c:\CobaltAlt\Source\Curse.Azeroth.Web\Content\js\Azeroth\AzerothTip.js */
var __tip = new CurseTip({
    Url: 'http://www.wowdb.com',
    Namespace: 'wowdb-tooltip',
    Paths: ['spells', 'items', 'npcs', 'quests', 'achievements', 'world-events', 'pet-abilities', 'currencies', 'wod-talents'],
    LoadingText: '<div class="wowdb-tooltip"><div class="db-tooltip"><div class="db-description" style="width: auto">Loading..</div></div></div>',
});

function WP_Stretch(container)
{
    var MAX_WIDTH = 600;

    container.find('.db-description').each(function () {
        /* For each tooltip */
        var tooltip = jQuery(this);
        var currentWidth = tooltip.width();
        var increased = false;

        tooltip.find('.tooltip-table tr, .db-title, h2, h3, .db-achievement-criteria > li').each(function () {
            var maxHeight = parseInt(jQuery(this).attr('data-height'), 10) || 25;

            while (jQuery(this).height() > maxHeight && currentWidth < MAX_WIDTH) {
                increased = true;
                currentWidth += 10;
                tooltip.width(currentWidth);
            }
        });

        if (increased)
            tooltip.width(currentWidth + 20);
    });
}


if (typeof Azeroth !== "undefined") {
    Azeroth.CurseTip = __tip;
}
